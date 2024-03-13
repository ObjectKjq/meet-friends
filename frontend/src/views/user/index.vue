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
      </el-table-column>
      <el-table-column label="角色" width="70">
        <template slot-scope="scope">
          <span v-if="scope.row.userRole == 0">用户</span>
          <span v-else-if="scope.row.userRole == 1">管理员</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间">
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
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :page-sizes="[3, 20, 30, 40]"
      :current-page="1"
      background
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      class="page">
    </el-pagination>

  </div>
</template>

<script>
import {search} from '@/api/user'
export default {
  data() {
    return {
      // 搜索发送的数据
      listQuery: {
        // 第几页
        page: 1,
        // 每页多少条数据
        limit: 3,
        // 搜索用户名
        username: undefined,
      },
      // 总共有多少条数据
      total: 100,
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
    // 根据用户名搜索数据
    handleFilter(){
      
    },
    // 添加用户
    handleCreate(){

    },
    // 选择每页展示多少条数据调用
    handleSizeChange(limit){
      this.listQuery.limit = limit;
      this.listQuery.page = 1;
      this.listQuery.title = undefined;
      
    },
    // 选择页面调用
    handleCurrentChange(page){
      this.listQuery.page = page;
      
    }
  },
  created(){
    // 初始化数据
    // this.listLoading = true;
    search(this.listQuery).then((res)=>{
      this.list = res.data;
    })
  }
}
</script>

<style scoped>
  .page{
    margin-top: 18px;
  }
</style>