<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.username" placeholder="用户名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
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
      <el-table-column prop="username" label="用户名">
      </el-table-column>
      <el-table-column prop="userAccount" label="账号">
      </el-table-column>
      <el-table-column label="头像" width="91">
        <template slot-scope="scope">
            <el-image 
              style="width: 70px; height: 70px"
              :src="scope.row.avatarUrl" 
              :preview-src-list="[scope.row.avatarUrl]"
              fit="cover">
            </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="gender" label="性别">
      </el-table-column>
      <el-table-column prop="phone" label="手机号">
      </el-table-column>
      <el-table-column prop="email" label="邮箱">
      </el-table-column>
      <el-table-column prop="userStatus" label="状态">
        <template slot-scope="scope">
          {{scope.row.userStatus == 0?'可用':'禁用'}}
        </template>
      </el-table-column>
      <el-table-column label="角色" width="70">
        <template slot-scope="scope">
          <span v-if="scope.row.userRole == 0">用户</span>
          <span v-else-if="scope.row.userRole == 1">管理员</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间">
        <template slot-scope="scope">
          {{UTCTime(scope.row.createTime)}}
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
import {search} from '@/api/user'
import { UTCTime } from '@/utils/UTCTime'
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
      search({
        userName: this.listQuery.username,
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

<style scoped>
  .page{
    margin-top: 18px;
  }
</style>