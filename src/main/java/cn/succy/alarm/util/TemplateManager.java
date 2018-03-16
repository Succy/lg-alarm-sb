package cn.succy.alarm.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.jfinal.kit.StrKit;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Succy
 * @date 2017-10-12 18:49
 **/
@Slf4j
public class TemplateManager {
    private static final Template TEMPLATE;
    private static final String DEFAULT_TEMPLATE_FILE = "message.tpl";
    private static final String BASE_TEMPLATE_PATH = "templates/";

    static {
        Engine engine = Engine.use();
        engine.setDevMode(true);
        String templateStr = BASE_TEMPLATE_PATH + DEFAULT_TEMPLATE_FILE;

        // 判断模板文件是否存在，如果没有配置alarm.template的话，模板默认为classpath:template/message.tpl
        // 如果文件不存在，抛出异常
        if (log.isDebugEnabled()) {
            log.debug("template: {}", templateStr);
        }
        if (!FileUtil.exist(templateStr)) {
            log.error("template file is not exist; please add a template file in classpath:template/message.tpl or set the real template file path to alarm.template key");
            throw new RuntimeException("template file is not exist; please add a template file in classpath:template/message.tpl or set the real template file path to alarm.template key");
        }
        String templatePath = FileUtil.getAbsolutePath(templateStr);
        int index = FileUtil.lastIndexOfSeparator(templatePath);
        // 取出模板文件的上一级目录
        String path = StrUtil.subPre(templatePath, index);
        // 取出模板文件
        String fileName = StrUtil.subSuf(templatePath, index + 1);
        // 设置模板文件所在的目录
        engine.setBaseTemplatePath(path);
        // 共享StrKit，方便在模板中使用
        engine.addSharedObject("StrKit", new StrKit());
        TEMPLATE = engine.getTemplate(fileName);
    }

    public static String getTemplateMsg(TemplateModel model) {
        return TEMPLATE.renderToString(model.toMap());
    }

    public static StringBuilder getTemplateMsgBuilder(TemplateModel model) {
        return TEMPLATE.renderToStringBuilder(model.toMap());
    }
}
