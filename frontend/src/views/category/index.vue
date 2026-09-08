<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h2>商品分类管理</h2>
      <el-button type="primary" @click="openAddDialog(null)">新增顶级分类</el-button>
    </div>

    <el-tree
        :data="treeData"
        node-key="id"
        default-expand-all
        :props="{ children: 'children', label: 'name' }"
        style="margin-top: 20px;"
    >
      <template #default="{ node, data }">
        <span style="display: flex; align-items: center; justify-content: space-between; width: 100%; padding-right: 20px;">
          <span>{{ node.label }}</span>
          <span>
            <el-button size="small" type="primary" @click.stop="openAddDialog(data)">新增子分类</el-button>
            <el-button size="small" @click.stop="openEditDialog(data)">编辑</el-button>
            <el-button size="small" type="danger" @click.stop="handleDelete(data.id)">删除</el-button>
          </span>
        </span>
      </template>
    </el-tree>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="分类名称" required>
          <el-input v-model="form.name" placeholder="请输入分类名称" />
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
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getCategoryTree, addCategory, updateCategory, deleteCategory } from '@/api/category.js';

const treeData = ref([]);
const dialogVisible = ref(false);
const dialogTitle = ref('');
const parentId = ref(0);
const form = reactive({
  id: null,
  name: '',
  sortOrder: 0,
  status: 1
});

const loadTree = async () => {
  try {
    const res = await getCategoryTree();
    if (res.code === 200) {
      treeData.value = res.data || [];
    }
  } catch (error) {
    ElMessage.error('加载分类数据失败');
  }
};

const openAddDialog = (data) => {
  dialogTitle.value = '新增分类';
  form.id = null;
  form.name = '';
  form.sortOrder = 0;
  form.status = 1;
  parentId.value = data ? data.id : 0;
  dialogVisible.value = true;
};

const openEditDialog = (data) => {
  dialogTitle.value = '编辑分类';
  Object.assign(form, data);
  parentId.value = data.parentId || 0;
  dialogVisible.value = true;
};

const submitForm = async () => {
  if (!form.name || form.name.trim() === '') {
    ElMessage.warning('分类名称不能为空');
    return;
  }
  try {
    const payload = { ...form, parentId: parentId.value };
    let res;
    if (form.id) {
      res = await updateCategory(payload);
    } else {
      res = await addCategory(payload);
    }
    if (res.code === 200) {
      ElMessage.success(form.id ? '编辑成功' : '新增成功');
      dialogVisible.value = false;
      loadTree();
    } else {
      ElMessage.error(res.msg || '操作失败');
    }
  } catch (error) {
    ElMessage.error('操作失败，请重试');
  }
};

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确认删除？如果有子分类将无法删除。', '提示', { type: 'warning' });
    const res = await deleteCategory(id);
    if (res.code === 200) {
      ElMessage.success('删除成功');
      loadTree();
    } else {
      ElMessage.error(res.msg || '删除失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败');
    }
  }
};

onMounted(() => {
  loadTree();
});
</script>

<style scoped>
:deep(.el-tree-node__content) {
  height: 40px;
}
</style>