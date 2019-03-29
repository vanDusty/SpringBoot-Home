/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: ExportExcelTest
 * Author:   zhangfan
 * Date:     2019-03-29 11:12
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.excel.controller;

import cn.van.excel.utils.ExcelData;
import cn.van.excel.utils.ExportExcelUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-03-29
 * @since 1.0.0
 */
@Controller
public class ExportExcelController {



    @GetMapping("/get")
    public void contextLoads(HttpServletResponse response) throws Exception{
        System.out.println("进入方法了！**************");
//        List<CompanyDO> companyDOList = testDao.selectCompany();
//        System.out.println(companyDOList.size());
        ExcelData data = new ExcelData();
        data.setTagName("公司信息");
        List<String> titles = new ArrayList();
        titles.add("公司名称");
        titles.add("联系电话（多个）");
//        titles.add("a3");
        data.setTableHead(titles);


        List<List<Object>> rows = new ArrayList();
        List<Object> row = new ArrayList();
//        row.add("11111111111");
        row.add("22222222222");
        row.add("3333333333");
        rows.add(row);
        List<Object> row1 = new ArrayList();
//        row.add("11111111111");
        row1.add("2");
        row1.add("3");
        rows.add(row1);

//        for (int j=0;j<companyDOList.size()-1; j++){
//            List<Object> row = new ArrayList();
//            row.add(companyDOList.get(j).getName());
//            row.add(companyDOList.get(j).getPhone());
//            rows.add(row);
//        }

//        for (CompanyDO companyDO : companyDOList) {
//            List<Object> row = new ArrayList();
//            row.add(companyDO.getName());
//            row.add(companyDO.getPhone());
//            rows.add(row);
//        }

        data.setData(rows);


        //生成本地
        /*File f = new File("c:/test.xlsx");
        FileOutputStream out = new FileOutputStream(f);
        ExportExcelUtils.exportExcel(data, out);
        out.close();*/
        ExportExcelUtils.exportExcel(response,"company.xlsx",data);

    }

}