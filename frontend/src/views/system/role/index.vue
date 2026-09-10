<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h2>角色管理</h2>
      <el-button type="primary" @click="openAddDialog">新增角色</el-button>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="roleCode" label="角色编码" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="380" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="openEditDialog(row)">编辑</el-button>
          <el-button size="small" type="success" @click="openAssignDialog(row)">分配权限</el-button>
          <el-button size="small" :type="row.status === 1 ? 'warning' : 'success'" @click="handleToggleStatus(row)">
            {{ row.status === 1 ? '禁用' : '启用' }}
          </el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="角色名称" required>
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" required>
          <el-input v-model="form.roleCode" placeholder="请输入角色编码（如 ROLE_MANAGER）" />
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

    <!-- 分配权限弹窗 -->
    <el-dialog v-model="assignVisible" title="分配权限" width="500px">
      <div style="margin-bottom: 10px; color: #909399; font-size: 13px;">
        当前角色：<strong>{{ currentRoleName }}</strong>
      </div>
      <el-tree
          ref="menuTreeRef"
          :data="menuTree"
          node-key="id"
          show-checkbox
          default-expand-all
          :props="{ children: 'children', label: 'menuName' }"
          style="max-height: 400px; overflow-y: auto; border: 1px solid #ebeef5; padding: 10px; border-radius: 4px;"
      />
      <template #footer>
        <el-button @click="assignVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAssign">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getRoleList, addRole, updateRole, deleteRole, toggleRoleStatus, getRoleMenus, assignMenus } from '@/api/role.js';
import { getMenuTree } from '@/api/menu.js';

const tableData = ref([]);
const dialogVisible = ref(false);
const dialogTitle = ref('');
const form = reactive({ id: null, roleName: '', roleCode: '', status: 1 });

const assignVisible = ref(false);
const menuTree = ref([]);
const menuTreeRef = ref(null);
const currentRoleId = ref(null);
const currentRoleName = ref('');

const loadData = async () => {
  const res = await getRoleList();
  if (res.code === 200) {
    tableData.value = res.data || [];
  }
};

const openAddDialog = () => {
  dialogTitle.value = '新增角色';
  Object.assign(form, { id: null, roleName: '', roleCode: '', status: 1 });
  dialogVisible.value = true;
};

const openEditDialog = (row) => {
  dialogTitle.value = '编辑角色';
  Object.assign(form, { ...row });
  dialogVisible.value = true;
};

const submitForm = async () => {
  if (!form.roleName) {
    ElMessage.warning('角色名称不能为空');
    return;
  }
  if (!form.roleCode) {
    ElMessage.warning('角色编码不能为空');
    return;
  }
  let res;
  if (form.id) {
    res = await updateRole(form);
  } else {
    res = await addRole(form);
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
  await ElMessageBox.confirm(`确认${action}角色「${row.roleName}」？`, '提示', { type: 'warning' });
  const res = await toggleRoleStatus(row.id);
  if (res.code === 200) {
    ElMessage.success(`${action}成功`);
    loadData();
  }
};

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确认删除该角色？', '提示', { type: 'warning' });
  const res = await deleteRole(id);
  if (res.code === 200) {
    ElMessage.success('删除成功');
    loadData();
  } else {
    ElMessage.error(res.msg || '删除失败');
  }
};

const openAssignDialog = async (row) => {
  currentRoleId.value = row.id;
  currentRoleName.value = row.roleName;

  const menuRes = await getMenuTree();
  if (menuRes.code === 200) {
    menuTree.value = menuRes.data || [];
  }

  const roleRes = await getRoleMenus(row.id);
  const checkedIds = (roleRes.code === 200 && roleRes.data) ? roleRes.data : [];

  assignVisible.value = true;

  setTimeout(() => {
    if (menuTreeRef.value) {
      menuTreeRef.value.setCheckedKeys([], false);
      checkedIds.forEach(id => {
        const node = menuTreeRef.value.getNode(id);
        if (node && node.isLeaf) {
          menuTreeRef.value.setChecked(id, true, false);
        }
      });
    }
  }, 200);
};

const submitAssign = async () => {
  if (!menuTreeRef.value) return;

  const checkedKeys = menuTreeRef.value.getCheckedKeys();
  const halfCheckedKeys = menuTreeRef.value.getHalfCheckedKeys();
  const allKeys = [...checkedKeys, ...halfCheckedKeys];

  const res = await assignMenus(currentRoleId.value, allKeys);
  if (res.code === 200) {
    ElMessage.success('权限分配成功');
    assignVisible.value = false;
  } else {
    ElMessage.error(res.msg || '分配失败');
  }
};

onMounted(loadData);
</script>