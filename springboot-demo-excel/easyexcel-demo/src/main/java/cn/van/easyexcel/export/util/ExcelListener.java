package cn.van.easyexcel.export.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ExcelListener
 *
 * @author: Van
 * Date:     2019-12-07 18:13
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Slf4j
public class ExcelListener extends AnalysisEventListener {

    /**
     * 自定义用于暂时存储data。
     */
    private List<Object> datas = new ArrayList<>();

    /**
     * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
     */
    @Override
    public void invoke(Object data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));

        //数据存储到list，供批量处理，或后续自己业务逻辑处理。
        datas.add(data);
        //根据自己业务做处理（通用业务可以不需要该项）
        doSomething(data);
    }

    private void doSomething(Object object) {
        log.info("doSomething.....");
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("所有数据解析完成！");
    }

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }
}
