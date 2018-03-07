package cn.succy.alarm.sender;


import cn.succy.alarm.util.TemplateModel;

public interface Sender {
    void send(TemplateModel model);
}
