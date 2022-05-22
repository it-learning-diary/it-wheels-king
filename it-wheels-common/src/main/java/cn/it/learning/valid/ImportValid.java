package cn.it.learning.valid;

import cn.hutool.core.util.StrUtil;
import cn.it.learning.annotation.ImportFieldValid;
import cn.it.learning.model.ImportCommonConstant;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * @Author it-learning
 * @Description 导入数据字段必填校验
 * @Date 2022/5/20 15:19
 * @Version 1.0
 */
public class ImportValid {

    /**
     * 检查导入的必填字段
     *
     * @param object
     * @param errorLog
     */
    public static void validRequireField(Object object, List<String> errorLog) {
        StringBuilder log = new StringBuilder();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 设置可访问属性
            field.setAccessible(Boolean.TRUE);
            Object fieldValue = null;
            boolean isValid = field.isAnnotationPresent(ImportFieldValid.class);
            if (isValid) {
                try {
                    fieldValue = field.get(object);
                } catch (Exception e) {
                    throw new RuntimeException("校验必填字段时出现异常," + e.getMessage());
                }
                // 字符串类型，需要判断为NULL和""的情况
                if (Objects.nonNull(fieldValue) && field.getType().getName().contains(ImportCommonConstant.STRING_TYPE)) {
                    if (StrUtil.isBlank(fieldValue.toString())) {
                        String message = field.getAnnotation(ImportFieldValid.class).message();
                        log.append(message).append(StrUtil.COMMA);
                    }
                } else {
                    if (Objects.isNull(fieldValue)) {
                        String message = field.getAnnotation(ImportFieldValid.class).message();
                        log.append(message).append(StrUtil.COMMA);
                    }
                }

            }
        }
        if (log.length() > ImportCommonConstant.ZERO) {
            errorLog.add(StrUtil.removeSuffix(log.toString(), StrUtil.COMMA));
        }
    }

}
