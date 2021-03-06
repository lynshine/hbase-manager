package com.leo.hbase.manager.system.service;

import java.util.List;

import com.leo.hbase.manager.system.domain.SysHbaseTable;

/**
 * HBaseService接口
 *
 * @author leojie
 * @date 2020-08-16
 */
public interface ISysHbaseTableService {
    /**
     * 查询HBase表
     *
     * @param tableId HBaseID
     * @return HBase
     */
    public SysHbaseTable selectSysHbaseTableById(Long tableId);


    /**
     * 查询HBase表
     *
     * @return HBase
     */
    public SysHbaseTable selectSysHbaseTableByName(String tableName);


    /**
     * 查询HBase列表
     *
     * @param sysHbaseTable HBase
     * @return HBase集合
     */
    public List<SysHbaseTable> selectSysHbaseTableList(SysHbaseTable sysHbaseTable);

    /**
     * 新增HBase
     *
     * @param sysHbaseTable HBase
     * @return 结果
     */
    public int insertSysHbaseTable(SysHbaseTable sysHbaseTable);

    /**
     * 修改HBase
     *
     * @param sysHbaseTable HBase
     * @return 结果
     */
    public int updateSysHbaseTable(SysHbaseTable sysHbaseTable);

    /**
     * 修改HBase表的状态
     *
     * @param sysHbaseTable HBase表
     * @return 结果
     */
    public int updateSysHbaseTableStatus(SysHbaseTable sysHbaseTable);


    /**
     * 修改HBase表的禁用状态
     *
     * @param sysHbaseTable HBase表
     * @return 结果
     */
    public int updateSysHbaseTableDisabledStatus(SysHbaseTable sysHbaseTable);


    /**
     * 批量删除HBase
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysHbaseTableByIds(String ids);

    /**
     * 删除HBase信息
     *
     * @param tableId HBaseID
     * @return 结果
     */
    public int deleteSysHbaseTableById(Long tableId);
}
