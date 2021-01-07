#!/bin/sh

CUR_SHELL_DIR=`pwd`
JAR_NAME="arknights-0.0.1-SNAPSHOT.jar"
JAR_PATH=$CUR_SHELL_DIR/$JAR_NAME
API_NAME=arknights

LOG_PATH="arknights.log"

#PID  代表是PID文件
PID=$API_NAME\.pid

#使用说明，用来提示输入参数
usage() {
    echo "Usage: sh 执行脚本.sh [start|stop|restart|status|download fileId|update jsonId]"
    exit 1
}

#检查程序是否在运行
is_exist(){
  pid=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}' `
  #如果不存在返回1，存在返回0     
  if [ -z "${pid}" ]; then
   return 1
  else
    return 0
  fi
}

#启动方法
start(){
  is_exist
  if [ $? -eq "0" ]; then 
    echo ">>> ${JAR_NAME} is already running PID=${pid} <<<" 
  else 
    nohup java -Xms256m -Xmx512m -jar $JAR_NAME >> $LOG_PATH 2>&1 &
    echo $! > $PID
    echo ">>> start $JAR_NAME successed PID=$! <<<" 
   fi
  }

#停止方法
stop(){
  #is_exist
  pidf=$(cat $PID)
  #echo "$pidf"  
  echo ">>> api PID = $pidf begin kill $pidf <<<"
  kill $pidf
  rm -rf $PID
  sleep 2
  is_exist
  if [ $? -eq "0" ]; then 
    echo ">>> api 2 PID = $pid begin kill -9 $pid  <<<"
    kill -9  $pid
    sleep 2
    echo ">>> $JAR_NAME process stopped <<<"  
  else
    echo ">>> ${JAR_NAME} is not running <<<"
  fi  
}

#输出运行状态
status(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> ${JAR_NAME} is running PID is ${pid} <<<"
  else
    echo ">>> ${JAR_NAME} is not running <<<"
  fi
}

#重启
restart(){
  stop
  start
}

#下载
download(){
  echo "文件ID为：$1"
  fileJson=`curl -X POST "http://localhost:8888/v1/LuaApiCaller?qq=3022645754&funcname=OidbSvc.0x6d6_2" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"GroupID\": 901158551, \"FileID\": \"$1\"}"`
  fileJson1=${fileJson#*Url\":\"}
  fileUrl=${fileJson1%\"*}
  echo "获取到文件Url为${fileUrl}"
  if [ ! -f "${JAR_NAME}.temple" ]; then
    mv ${JAR_NAME} "${JAR_NAME}.temple"
  else
    rm -f "${JAR_NAME}.temple"
    mv ${JAR_NAME} "${JAR_NAME}.temple"
  fi
  wget -c ${fileUrl} -O ${JAR_NAME}
  restart
}

##更新
update(){
  echo "更新json编号为：$1"
  curl -X GET "http://localhost:8086/Update/AutoUpdate?JsonId=3204855389061" -H "accept: */*"
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  "download")
    download $2
    ;;
  "update")
    update $2
    ;;
  *)
    usage
    ;;
esac
exit 0
