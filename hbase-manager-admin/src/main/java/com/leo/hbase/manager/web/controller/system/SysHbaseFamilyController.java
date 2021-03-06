package com.leo.hbase.manager.web.controller.system;

import com.leo.hbase.manager.adaptor.model.FamilyDesc;
import com.leo.hbase.manager.adaptor.service.IHBaseAdminService;
import com.leo.hbase.manager.common.annotation.Log;
import com.leo.hbase.manager.common.core.controller.BaseController;
import com.leo.hbase.manager.common.core.domain.AjaxResult;
import com.leo.hbase.manager.common.core.page.TableDataInfo;
import com.leo.hbase.manager.common.enums.BusinessType;
import com.leo.hbase.manager.common.enums.HBaseReplicationScopeFlag;
import com.leo.hbase.manager.common.utils.poi.ExcelUtil;
import com.leo.hbase.manager.system.domain.SysHbaseTable;
import com.leo.hbase.manager.system.dto.FamilyDescDto;
import com.leo.hbase.manager.system.service.ISysHbaseTableService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HBase FamilyController
 *
 * @author leojie
 * @date 2020-08-22
 */
@Controller
@RequestMapping("/system/family")
public class SysHbaseFamilyController extends BaseController {
    private String prefix = "system/family";

    @Autowired
    private ISysHbaseTableService sysHbaseTableService;
    @Autowired
    private IHBaseAdminService ihBaseAdminService;

    @RequiresPermissions("system:family:view")
    @GetMapping()
    public String family() {
        return prefix + "/family";
    }

    /**
     * 查询HBase Family列表
     */
    @RequiresPermissions("system:family:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysHbaseTable sysHbaseTable) {
        SysHbaseTable existsTable = sysHbaseTableService.selectSysHbaseTableById(sysHbaseTable.getTableId());
        final List<FamilyDesc> familyDescList = ihBaseAdminService.getFamilyDesc(existsTable.getTableName());
        final List<FamilyDescDto> familyDescDtoList = familyDescList.stream().map(familyDesc -> {
            FamilyDescDto familyDescDto = new FamilyDescDto().convertFor(familyDesc);
            familyDescDto.setTableName(existsTable.getTableName());
            return familyDescDto;
        }).collect(Collectors.toList());
        return getDataTable(familyDescDtoList);
    }

    /**
     * 导出HBase Family列表
     */
    @RequiresPermissions("system:family:export")
    @Log(title = "HBase Family", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysHbaseTable sysHbaseTable) {
        SysHbaseTable existsTable = sysHbaseTableService.selectSysHbaseTableById(sysHbaseTable.getTableId());
        final List<FamilyDesc> familyDescList = ihBaseAdminService.getFamilyDesc(existsTable.getTableName());
        final List<FamilyDescDto> familyDescDtoList = familyDescList.stream().map(familyDesc -> {
            FamilyDescDto familyDescDto = new FamilyDescDto().convertFor(familyDesc);
            familyDescDto.setTableName(existsTable.getTableName());
            return familyDescDto;
        }).collect(Collectors.toList());
        ExcelUtil<FamilyDescDto> util = new ExcelUtil<>(FamilyDescDto.class);
        return util.exportExcel(familyDescDtoList, "family");

    }

    /**
     * 新增HBase Family
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存HBase Family
     */
    @RequiresPermissions("system:family:add")
    @Log(title = "HBase Family", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FamilyDescDto familyDescDto) {
        return error("暂不支持列簇的新增");
    }

    /**
     * 修改HBase Family
     */
    @GetMapping("/edit/{familyId}")
    public String edit(@PathVariable("familyId") String familyId, ModelMap mmap) {
        String tableName = familyId.substring(0, familyId.lastIndexOf(":"));
        String familyName = familyId.substring(familyId.lastIndexOf(":") + 1);

        final List<FamilyDesc> familyDescList = ihBaseAdminService.getFamilyDesc(tableName);
        final List<FamilyDescDto> familyDescDtoList = familyDescList.stream()
                .filter(familyDesc -> familyDesc.getFamilyName().equals(familyName))
                .map(familyDesc -> new FamilyDescDto().convertFor(familyDesc))
                .collect(Collectors.toList());
        final FamilyDescDto familyDescDto = familyDescDtoList.get(0);
        familyDescDto.setTableName(tableName);
        familyDescDto.setFamilyId(familyName);
        mmap.put("familyDescDto", familyDescDto);
        return prefix + "/edit";
    }

    /**
     * 修改保存HBase Family
     */
    @RequiresPermissions("system:family:edit")
    @Log(title = "HBase Family", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FamilyDescDto familyDescDto) {
        final Integer replicationScope = familyDescDto.getReplicationScope();

        if(replicationScope.toString().equals(HBaseReplicationScopeFlag.CLOSE.getCode())){
            ihBaseAdminService.disableReplication(familyDescDto.getTableName(), Collections.singletonList(familyDescDto.getFamilyId()));
        }

        if(replicationScope.toString().equals(HBaseReplicationScopeFlag.OPEN.getCode())){
            ihBaseAdminService.enableReplication(familyDescDto.getTableName(), Collections.singletonList(familyDescDto.getFamilyId()));
        }
        return success();
    }

    /**
     * 删除HBase Family
     */
    @RequiresPermissions("system:family:remove")
    @Log(title = "HBase Family", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return error("暂不支持列簇删除！");
    }
}
