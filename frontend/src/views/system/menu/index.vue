<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h2>菜单管理</h2>
      <el-button type="primary" @click="openAddDialog(null)">新增顶级菜单</el-button>
    </div>

    <el-tree
        :data="treeData"
        node-key="id"
        default-expand-all
        :props="{ children: 'children', label: 'menuName' }"
        style="margin-top: 20px;"
    >
      <template #default="{ node, data }">
        <span style="display: flex; align-items: center; justify-content: space-between; width: 100%; padding-right: 20px;">
          <span>{{ node.label }}</span>
          <span>
            <el-button size="small" type="primary" @click.stop="openAddDialog(data)">新增子菜单</el-button>
            <el-button size="small" @click.stop="openEditDialog(data)">编辑</el-button>
            <el-button size="small" type="danger" @click.stop="handleDelete(data.id)">删除</el-button>
          </span>
        </span>
      </template>
    </el-tree>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="上级菜单" v-if="form.parentId !== undefined">
          <el-input :value="parentName" disabled />
        </el-form-item>
        <el-form-item label="菜单名称" required>
          <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="路由路径">
          <el-input v-model="form.path" placeholder="如 /system/user" />
        </el-form-item>
        <el-form-item label="组件路径">
          <el-input v-model="form.component" placeholder="如 system/user/index" />
        </el-form-item>
        <el-form-item label="权限标识">
          <el-input v-model="form.perms" placeholder="如 user:list" />
        </el-form-item>
        <el-form-item label="菜单类型">
          <el-radio-group v-model="form.menuType">
            <el-radio :label="1">目录</el-radio>
            <el-radio :label="2">菜单</el-radio>
            <el-radio :label="3">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="form.icon" placeholder="如 el-icon-setting" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
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
import { ref, reactive, onMounted, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getMenuTree, addMenu, updateMenu, deleteMenu } from '@/api/menu.js';

const treeData = ref([]);
const dialogVisible = ref(false);
const dialogTitle = ref('');
const parentName = computed(() => {
  if (form.parentId === 0 || form.parentId === null) return '顶级菜单';
  const parent = findNode(treeData.value, form.parentId);
  return parent ? parent.menuName : '未知';
});

const form = reactive({
  id: null,
  parentId: 0,
  menuName: '',
  path: '',
  component: '',
  perms: '',
  menuType: 1,
  icon: '',
  sortOrder: 0,
  status: 1
});

const loadTree = async () => {
  const res = await getMenuTree();
  if (res.code === 200) {
    treeData.value = res.data || [];
  }
};

const findNode = (tree, id) => {
  for (const node of tree) {
    if (node.id === id) return node;
    if (node.children) {
      const found = findNode(node.children, id);
      if (found) return found;
    }
  }
  return null;
};

const openAddDialog = (data) => {
  dialogTitle.value = data ? '新增子菜单' : '新增顶级菜单';
  form.id = null;
  form.parentId = data ? data.id : 0;
  form.menuName = '';
  form.path = '';
  form.component = '';
  form.perms = '';
  form.menuType = data ? 2 : 1; // 子菜单默认菜单类型为“菜单”
  form.icon = '';
  form.sortOrder = 0;
  form.status = 1;
  dialogVisible.value = true;
};

const openEditDialog = (data) => {
  dialogTitle.value = '编辑菜单';
  Object.assign(form, { ...data });
  dialogVisible.value = true;
};

const submitForm = async () => {
  if (!form.menuName) {
    ElMessage.warning('菜单名称不能为空');
    return;
  }
  let res;
  if (form.id) {
    res = await updateMenu(form);
  } else {
    res = await addMenu(form);
  }
  if (res.code === 200) {
    ElMessage.success(form.id ? '编辑成功' : '新增成功');
    dialogVisible.value = false;
    loadTree();
  } else {
    ElMessage.error(res.msg || '操作失败');
  }
};

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确认删除该菜单？如果有子菜单将无法删除。', '提示', { type: 'warning' });
  const res = await deleteMenu(id);
  if (res.code === 200) {
    ElMessage.success('删除成功');
    loadTree();
  } else {
    ElMessage.error(res.msg || '删除失败');
  }
};

onMounted(loadTree);
</script>