<template>
  <div>
    <!-- 搜索区域 -->
    <el-form :inline="true" :model="searchForm">
      <el-form-item label="关键词">
        <el-input v-model="searchForm.keyword" placeholder="SKU/名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
        <el-button type="success" @click="openAddDialog">新增商品</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="skuCode" label="SKU编码" width="120" />
      <el-table-column prop="name" label="商品名称" />
      <el-table-column prop="categoryName" label="所属分类" />
      <el-table-column prop="spec" label="规格" />
      <el-table-column prop="unit" label="单位" width="80" />
      <el-table-column prop="warningThreshold" label="预警阈值" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="openEditDialog(row)">编辑</el-button>
          <el-button size="small" :type="row.status === 1 ? 'warning' : 'success'" @click="handleToggleStatus(row)">
            {{ row.status === 1 ? '禁用' : '启用' }}
          </el-button>
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
      <el-form :model="form" label-width="100px">
        <el-form-item label="SKU编码" required>
          <el-input v-model="form.skuCode" placeholder="如 SKU001" />
        </el-form-item>
        <el-form-item label="商品名称" required>
          <el-input v-model="form.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="所属分类">
          <el-select v-model="form.categoryId" placeholder="请选择分类" clearable style="width: 100%">
            <el-option
                v-for="item in categoryList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="form.spec" placeholder="如 128GB" />
        </el-form-item>
        <el-form-item label="单位">
          <el-select v-model="form.unit" placeholder="请选择单位" style="width: 100%">
            <el-option label="个" value="个" />
            <el-option label="件" value="件" />
            <el-option label="箱" value="箱" />
            <el-option label="台" value="台" />
            <el-option label="公斤" value="公斤" />
            <el-option label="米" value="米" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警阈值">
          <el-input-number v-model="form.warningThreshold" :min="0" />
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
import { getProductPage, addProduct, updateProduct, deleteProduct, toggleProductStatus } from '@/api/product.js';
import { getAllCategories } from '@/api/category.js';

const searchForm = reactive({ keyword: '' });
const tableData = ref([]);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);
const categoryList = ref([]);

const dialogVisible = ref(false);
const dialogTitle = ref('');
const form = reactive({
  id: null,
  skuCode: '',
  name: '',
  categoryId: null,
  spec: '',
  unit: '',
  warningThreshold: 0,
  status: 1
});

const loadData = async () => {
  const res = await getProductPage({
    keyword: searchForm.keyword,
    pageNum: pageNum.value,
    pageSize: pageSize.value
  });
  if (res.code === 200) {
    tableData.value = res.data.list || [];
    total.value = res.data.total || 0;
  }
};

const loadCategories = async () => {
  const res = await getAllCategories();
  if (res.code === 200) {
    categoryList.value = res.data || [];
  }
};

const resetSearch = () => {
  searchForm.keyword = '';
  pageNum.value = 1;
  loadData();
};

const openAddDialog = () => {
  dialogTitle.value = '新增商品';
  Object.assign(form, { id: null, skuCode: '', name: '', categoryId: null, spec: '', unit: '', warningThreshold: 0, status: 1 });
  dialogVisible.value = true;
};

const openEditDialog = (row) => {
  dialogTitle.value = '编辑商品';
  Object.assign(form, { ...row });
  dialogVisible.value = true;
};

const submitForm = async () => {
  if (!form.skuCode || form.skuCode.trim() === '') {
    ElMessage.warning('SKU编码不能为空');
    return;
  }
  if (!form.name || form.name.trim() === '') {
    ElMessage.warning('商品名称不能为空');
    return;
  }
  let res;
  if (form.id) {
    res = await updateProduct(form);
  } else {
    res = await addProduct(form);
  }
  if (res.code === 200) {
    ElMessage.success('操作成功');
    dialogVisible.value = false;
    loadData();
  }
};

const handleToggleStatus = async (row) => {
  const action = row.status === 1 ? '禁用' : '启用';
  await ElMessageBox.confirm(`确认${action}商品「${row.name}」？`, '提示', { type: 'warning' });
  const res = await toggleProductStatus(row.id);
  if (res.code === 200) {
    ElMessage.success(`${action}成功`);
    loadData();
  }
};

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确认删除该商品？', '提示', { type: 'warning' });
  const res = await deleteProduct(id);
  if (res.code === 200) {
    ElMessage.success('删除成功');
    loadData();
  }
};

onMounted(() => {
  loadCategories();
  loadData();
});
</script>