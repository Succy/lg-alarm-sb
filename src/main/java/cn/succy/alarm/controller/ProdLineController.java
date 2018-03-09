package cn.succy.alarm.controller;

import cn.succy.alarm.entity.ProdLine;
import cn.succy.alarm.service.ProdLineService;
import cn.succy.alarm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Controller
@RequestMapping("/prodline")
public class ProdLineController {
    @Autowired
    private ProdLineService prodLineService;

    @GetMapping("/all")
    public String findProdLineAll(Model model) {
        List<ProdLine> prodLineList = prodLineService.findAll();
        model.addAttribute(prodLineList);
        return "pages/prod_line";
    }

    @GetMapping("/getAddView")
    public String getAddProdLineView() {

        return "pages/add_prodline";
    }

    @GetMapping("/getEditView/{id}")
    public String getEditView(@PathVariable("id") Integer id, Model model) {
        ProdLine prodLine = prodLineService.findById(id);
        model.addAttribute(prodLine);
        return "pages/add_prodline";
    }

    @PostMapping("/save")
    @ResponseBody
    public Result<ProdLine> save(@ModelAttribute ProdLine prodLine) {
        // 由于前端也是本人写的，因此，我相信自己的前端数据，不做校验
        ProdLine save = prodLineService.save(prodLine);
        return new Result<>(0, "新增产线信息成功", save);
    }

    @PostMapping("/edit/{id}")
    @ResponseBody
    public Result<Void> edit(@ModelAttribute ProdLine prodLine, @PathVariable("id") Integer id) {
        //先查询数据库，如果对应id的产线被另外一个删掉了，就不需要更新了
        Result<Void> result = new Result<>();
        ProdLine prodLine1 = prodLineService.findById(id);
        if (prodLine1 == null) {
            result.setCode(-1);
            result.setMsg(String.format("要修改的产线(id=%s)不存在，请刷新看看", id));
            return result;
        }
        prodLine.setId(id);
        prodLineService.save(prodLine);

        result.setCode(0);
        result.setMsg("修改成功！");
        return result;
    }
    @DeleteMapping("/del/{id}")
    @ResponseBody
    public Result<Void> delete(@PathVariable("id") Integer id) {
        Result<Void> result = new Result<>();
        // 先查询数据库
        ProdLine prodLine = prodLineService.findById(id);
        if (prodLine == null) {
            result.setMsg(String.format("要删除的产线(id=%s)不存在，请刷新看看", id));
            result.setCode(-1);
            return result;
        }

        prodLineService.delById(id);
        result.setCode(0);
        result.setMsg("删除成功！");
        return result;
    }
}
