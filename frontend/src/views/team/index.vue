<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.username" placeholder="队伍名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-edit" @click="handleCreate">
        添加
      </el-button>
    </div>
    
    <el-table
    ref="multipleTable"
    v-loading="listLoading"
    :data="list"
    border
    fit
    highlight-current-row
    stripe
    style="width: 100%;"
    >
      <el-table-column prop="id" label="ID" width="55">
      </el-table-column>
      <el-table-column prop="name" label="队伍名称">
      </el-table-column>
      <el-table-column prop="description" label="描述">
      </el-table-column>
      <el-table-column prop="maxNum" label="最大人数">
      </el-table-column>
      <el-table-column label="过期时间">
        <template slot-scope="scope">
          {{UTCTime(scope.row.expireTime)}}
        </template>
      </el-table-column>
      <el-table-column prop="userId" label="队长">
      </el-table-column>
      <el-table-column label="状态">
        <template slot-scope="scope">
          {{scope.row.status == 0 ? '公开':scope.row.status == 1?'私有':'加密'}}
        </template>
      </el-table-column>
      <el-table-column label="密码" width="70">
        <template slot-scope="scope">
          {{scope.row.password}}
        </template>
      </el-table-column>
      <el-table-column label="创建时间">
        <template slot-scope="scope">
          {{UTCTime(scope.row.createTime)}}
        </template>
      </el-table-column>
      <el-table-column label="修改时间">
        <template slot-scope="scope">
          {{UTCTime(scope.row.updateTime)}}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="primary"  @click="handleEdit(scope.$index, scope.row)">
            编辑
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      @current-change="handleCurrentChange"
      :page-size="listQuery.limit"
      :current-page="1"
      background
      layout="total, prev, pager, next, jumper"
      :total="total"
      class="page">
    </el-pagination>
  </div>
</template>

<script>
import { UTCTime } from '@/utils/UTCTime'
import { listTeamsByPage } from '@/api/team'
export default {
data() {
    return {
      // 搜索发送的数据
      listQuery: {
        // 第几页
        page: 1,
        // 每页多少条数据
        limit: 5,
        // 搜索用户名
        username: undefined,
      },
      // 总共有多少条数据
      total: 0,
      // 列表是否在加载中
      listLoading: false,
      // 数据绑定到这里
      list:[],
    }
  },
  methods: {
    // 编辑信息
    handleEdit(){
      
    },
    // 删除数据
    handleDelete(){
      
    },
    // 添加用户
    handleCreate(){

    },
    // 根据用户名搜索数据
    handleFilter(){
      this.initList();
    },
    // 选择页面调用
    handleCurrentChange(page){
      this.listQuery.page = page;
      this.initList();
    },
    // 初始化数据
    initList(){
      // 初始化数据
      this.listLoading = true;
      listTeamsByPage({
        name: this.listQuery.username,
        current: this.listQuery.page,
        pageSize: this.listQuery.limit,
      }).then((res)=>{
        this.list = res.data.records;
        this.total = res.data.total;
        this.listLoading = false;
      })
    },
    UTCTime,
  },
  created(){
    this.initList();
  }
}
</script>

<style>

</style>