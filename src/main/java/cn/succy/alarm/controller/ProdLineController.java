package cn.succy.alarm.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.succy.alarm.entity.ProdLine;
import cn.succy.alarm.service.ContactService;
import cn.succy.alarm.service.ProdLineService;
import cn.succy.alarm.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import java.util.List;

@Controller
@RequestMapping("/prodline")
@Slf4j
public class ProdLineController {
    @Autowired
    private ProdLineService prodLineService;

    @Autowired
    private ContactService contactService;

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

    @GetMapping("/getConfView/{id}")
    public String getConfView(@PathVariable Integer id, Model model) {
        String contactMap4ZTree = contactService.getContactMap4ZTree(id);
        model.addAttribute("nodeStr", contactMap4ZTree);
        model.addAttribute("id", id);
        return "pages/associate";
    }

    @PostMapping("/add")
    @ResponseBody
    public Result<ProdLine> save(@ModelAttribute ProdLine prodLine) {
        // 由于前端也是本人写的，因此，我相信自己的前端数据，不做校验
        ProdLine save = prodLineService.save(prodLine);
        return new Result<>(0, "新增产线信息成功", save);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public Result<Void> update(@ModelAttribute ProdLine prodLine, @PathVariable("id") Integer id) {
        //先查询数据库，如果对应id的产线被另外一个删掉了，就不需要更新了
        Result<Void> result = new Result<>();
        ProdLine oldProdLine = prodLineService.findById(id);
        if (oldProdLine == null) {
            result.setCode(-1);
            result.setMsg(String.format("要修改的产线(id=%s)不存在，请刷新看看", id));
            return result;
        }

        BeanUtil.copyProperties(prodLine, oldProdLine, CopyOptions.create().setIgnoreNullValue(true));
        prodLineService.update(oldProdLine);

        result.setCode(0);
        result.setMsg("修改成功！");
        return result;
    }

    @DeleteMapping("/del/{id}")
    @ResponseBody
    public Result<Void> delete(@PathVariable("id") Integer id) {
        Result<Void> result = new Result<>();
        // 直接删除，若不存在，del语句也是可以执行成功的。省去先查询的开销
        // 当且仅当抛出异常时，认定删除失败
        result.setCode(0);
        result.setMsg("删除成功！");
        try {
            prodLineService.delById(id);
        } catch (Exception e) {
            result.setCode(-1);
            result.setMsg("删除失败，失败原因：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/conf/{id}")
    @ResponseBody
    public Result<Void> config(@RequestParam(value = "contactIds[]", required = false) String[] ids,
                               @PathVariable Integer id) {
        Result<Void> result = new Result<>(0, "配置成功！", null);
        try {
            prodLineService.config(ids, id);
        } catch (Exception e) {
            result.setCode(-1);
            result.setMsg("配置失败，原因：" + e.getMessage());
            log.warn("配置失败", e);
        }
        return result;
    }
}
