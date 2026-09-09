<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h2>用户管理</h2>
      <el-button type="primary" @click="openAddDialog">新增用户</el-button>
    </div>

    <!-- 搜索 -->
    <el-form :inline="true" :model="searchForm" style="margin-bottom: 20px;">
      <el-form-item label="关键词">
        <el-input v-model="searchForm.keyword" placeholder="用户名/昵称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="nickname" label="昵称" />
      <el-table-column prop="roleName" label="角色" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="320" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="openEditDialog(row)">编辑</el-button>
          <el-button size="small" :type="row.status === 1 ? 'warning' : 'success'" @click="handleToggleStatus(row)">
            {{ row.status === 1 ? '禁用' : '启用' }}
          </el-button>
          <el-button size="small" type="info" @click="handleResetPwd(row.id)">重置密码</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="loadData"
        @current-change="loadData"
        style="margin-top: 20px; justify-content: flex-end;"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名" required>
          <el-input v-model="form.username" placeholder="请输入用户名" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="角色" required>
          <el-select v-model="form.roleId" placeholder="请选择角色" style="width: 100%;">
            <el-option
                v-for="item in roleList"
                :key="item.id"
                :label="item.roleName"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="密码" v-if="!form.id">
          <el-input v-model="form.password" placeholder="请输入密码（默认 123456）" show-password />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getUserPage, addUser, updateUser, toggleUserStatus, resetUserPassword, deleteUser } from '@/api/user.js';
import { getRoleList } from '@/api/role.js';

const searchForm = reactive({ keyword: '' });
const tableData = ref([]);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);
const roleList = ref([]);

const dialogVisible = ref(false);
const dialogTitle = ref('');
const form = reactive({ id: null, username: '', nickname: '', roleId: null, password: '', status: 1 });

const loadData = async () => {
  const res = await getUserPage({
    keyword: searchForm.keyword,
    pageNum: pageNum.value,
    pageSize: pageSize.value
  });
  if (res.code === 200) {
    tableData.value = res.data.list || [];
    total.value = res.data.total || 0;
  }
};

const loadRoles = async () => {
  const res = await getRoleList();
  if (res.code === 200) {
    roleList.value = res.data || [];
  }
};

const resetSearch = () => {
  searchForm.keyword = '';
  pageNum.value = 1;
  loadData();
};

const openAddDialog = () => {
  dialogTitle.value = '新增用户';
  Object.assign(form, { id: null, username: '', nickname: '', roleId: null, password: '', status: 1 });
  dialogVisible.value = true;
};

const openEditDialog = (row) => {
  dialogTitle.value = '编辑用户';
  Object.assign(form, { ...row, password: '' });
  dialogVisible.value = true;
};

const submitForm = async () => {
  if (!form.username) {
    ElMessage.warning('用户名不能为空');
    return;
  }
  if (!form.roleId) {
    ElMessage.warning('请选择角色');
    return;
  }
  let res;
  if (form.id) {
    const payload = { id: form.id, username: form.username, nickname: form.nickname, roleId: form.roleId, status: form.status };
    res = await updateUser(payload);
  } else {
    const payload = { ...form, password: form.password || '123456' };
    res = await addUser(payload);
  }
  if (res.code === 200) {
    ElMessage.success(form.id ? '编辑成功' : '新增成功');
    dialogVisible.value = false;
    loadData();
  } else {
    ElMessage.error(res.msg || '操作失败');
  }
};

const handleToggleStatus = async (row) => {
  const action = row.status === 1 ? '禁用' : '启用';
  await ElMessageBox.confirm(`确认${action}用户「${row.username}」？`, '提示', { type: 'warning' });
  const res = await toggleUserStatus(row.id);
  if (res.code === 200) {
    ElMessage.success(`${action}成功`);
    loadData();
  }
};

const handleResetPwd = async (id) => {
  await ElMessageBox.confirm('确认重置该用户密码？重置后密码为 123456', '提示', { type: 'warning' });
  const res = await resetUserPassword(id);
  if (res.code === 200) {
    ElMessage.success('密码已重置为 123456');
    loadData();
  }
};

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确认删除该用户？', '提示', { type: 'warning' });
  const res = await deleteUser(id);
  if (res.code === 200) {
    ElMessage.success('删除成功');
    loadData();
  }
};

onMounted(() => {
  loadData();
  loadRoles();
});
</script>