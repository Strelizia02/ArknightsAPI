package com.strelizia.arknights.dao;

import com.strelizia.arknights.model.QuestionInfo;

public interface QuestionMapper {
    //获取一个完整的问题
    QuestionInfo getQuestionById(Integer id);
    //获取一个问题的答案
    String getAnswerById(Integer id);
    //更新当前群的问题状态
    Integer updateGroupQuestion(Long groupId, Integer questionId);
    //回答正确，关闭问题模式
    Integer closeGroupQuestionStatus(Long groupId);
    //更新一个问题
    Integer updateQuestion(QuestionInfo questionInfo);
    //插入一个问题
    Integer insertQuestion(QuestionInfo questionInfo);
}
