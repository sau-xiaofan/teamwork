package edu.sau.NetworkLotterySystem.controller;

import edu.sau.NetworkLotterySystem.entity.Page;
import edu.sau.NetworkLotterySystem.entity.PageData;
import edu.sau.NetworkLotterySystem.util.ObjectExcelView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping("formula")
public class formulaController extends BaseController{

    private int showCount;
    private int range;
    private int num;
    private int currentPage;
    private ArrayList<PageData> varList;
    private ArrayList<String> types;

    private int randIntNum(int a) {
        return new Random().nextInt(a) + 1;
    }

    private double randDoubleNum(int a) {
        double ans = new Random().nextDouble() * a;
        while(ans == 0) ans = new Random().nextDouble() * a;
        return ans;
    }


    @RequestMapping(value = "/listFormula")
    @ResponseBody
    public Object listFormula(@RequestBody PageData pd) throws Exception {
        Page page = new Page();
        Map<String, Object> map = new HashMap<String, Object>();
        String errInfo = "success";
        num = (int) pd.get("num");
        types = (ArrayList<String>) pd.get("types");
        boolean door = false;
        boolean doubleNum = false;
        boolean bracket = false;
        for(int i = 0; i < types.size();  ) {
            if(types.get(i).equals("+") || types.get(i).equals("-") || types.get(i).equals("*") || types.get(i).equals("/")) {
                door = true;
                i ++;
            }
            else if(types.get(i).equals("小数")) {
                doubleNum = true;
                types.remove(i);
            }
            else if(types.get(i).equals("括号")) {
                bracket = true;
                types.remove(i);
            }
        }
        page.setPd(pd);
        if(!door) {
            errInfo = "error";
            map.put("result", errInfo);
            map.put("message", "请至少选择 + - * / 中的一种符号");
            return map;
        }
        showCount  = (int) pd.get("showCount");
        range = Integer.parseInt(pd.getString("range"));
        currentPage = (int) pd.get("currentPage");
        int pointNum = 0;
        if(pd.get("doubleNum") != null) {
            pointNum = (int) pd.get("doubleNum");
        }
        varList = new ArrayList<PageData>();
        String format = "0.";
        for(int i = 0; i < pointNum; i ++ ){
            format += "0";
        }
        boolean has_bracket = false;
        int befor_num = 0x3f3f3f3f;
        DecimalFormat df = new DecimalFormat(format);
        for(int i = 0; i < num; i ++ ){
            has_bracket = false;
            PageData data = new PageData();
            data.put("id", i + 1);
            String ans = "";
            int numberSum = randIntNum(10);
            if((new Random().nextInt(2)) == 0 && numberSum != 1 && bracket == true) {
                ans += "(";
                has_bracket = true;
            }
            befor_num = 0;
            if(doubleNum) {
                double res = Double.parseDouble(df.format(randDoubleNum(range)));
                ans += res;
                for(int j = 0; j < numberSum; j ++ ) {
                    int symbol = randIntNum(types.size()) - 1;
                    ans += types.get(symbol);
                    if((new Random().nextInt(2)) == 0 && bracket == true && j != numberSum - 1 && j - befor_num > 1) {
                        if(has_bracket == false) {
                            ans += "(";
                            has_bracket = true;
                            befor_num = j;
                        }
                    }
                    res = Double.parseDouble(df.format(randDoubleNum(range)));
                    ans += res;
                    if((new Random().nextInt(2)) == 0 && bracket == true && j - befor_num> 1) {
                        if(has_bracket != false) {
                            ans += ")";
                            has_bracket = false;
                            befor_num = j;
                        }
                    }
                }
            } else {
                int res = randIntNum(range);
                ans += res;
                for(int j = 0; j < numberSum; j ++ ) {
                    int symbol = randIntNum(types.size()) - 1;
                    ans += types.get(symbol);
                    if((new Random().nextInt(2)) == 0 && bracket == true && j != numberSum - 1 && j - befor_num> 1) {
                        if(has_bracket == false) {
                            ans += "(";
                            has_bracket = true;
                            befor_num = j;
                        }
                    }
                    res = randIntNum(range);
                    ans += res;
                    if((new Random().nextInt(2)) == 0 && bracket == true && j - befor_num> 1) {
                        if(has_bracket != false) {
                            ans += ")";
                            has_bracket = false;
                            befor_num = j;
                        }
                    }
                }
            }
            if(has_bracket == true) {
                ans += ")";
            }
            data.put("formula", ans);
            varList.add(data);
        }
        page.setTotalResult(num);
        map.put("varList", varList);
        map.put("page", page);
        map.put("result", errInfo);
        return map;
    }

    @RequestMapping(value="/excel")
    public ModelAndView exportExcel(@RequestBody PageData pd) throws Exception{
        ModelAndView mv = new ModelAndView();
        Map<String,Object> dataMap = new HashMap<String,Object>();
        List<String> titles = new ArrayList<String>();
        titles.add("序号");	//1
        titles.add("四则运算式");	//2
        dataMap.put("titles", titles);
        List<LinkedHashMap> varOList = (List<LinkedHashMap>) pd.get("varList");
        List<PageData> varList = new ArrayList<PageData>();
        for(int i=0;i<varOList.size();i++){
            PageData vpd = new PageData();
            LinkedHashMap data = varOList.get(i);
            vpd.put("var1", data.get("id").toString());	    //1
            vpd.put("var2", data.get("formula"));	    //2
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv,dataMap);
        return mv;
    }


}
