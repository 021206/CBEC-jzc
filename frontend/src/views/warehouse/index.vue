<template>
  <div>
    <!-- 搜索区域 -->
    <el-form :inline="true" :model="searchForm" style="margin-bottom: 20px;">
      <el-form-item label="仓库名称">
        <el-input v-model="searchForm.keyword" placeholder="请输入仓库名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
        <el-button type="success" @click="openAddDialog">新增仓库</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="仓库名称" />
      <el-table-column prop="address" label="地址" />
      <el-table-column prop="contact" label="联系人" />
      <el-table-column prop="phone" label="联系电话" />
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
      <el-form :model="form" label-width="80px">
        <el-form-item label="仓库名称" required>
          <el-input v-model="form.name" placeholder="请输入仓库名称" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="form.contact" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
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
import { getWarehousePage, addWarehouse, updateWarehouse, deleteWarehouse, toggleWarehouseStatus } from '@/api/warehouse.js';

// ---------- 搜索 ----------
const searchForm = reactive({ keyword: '' });
const handleSearch = () => { pageNum.value = 1; loadData(); };
const resetSearch = () => { searchForm.keyword = ''; handleSearch(); };

// ---------- 表格数据 ----------
const tableData = ref([]);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);

const loadData = async () => {
  try {
    const res = await getWarehousePage({
      keyword: searchForm.keyword,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    });
    if (res.code === 200) {
      tableData.value = res.data.list || [];
      total.value = res.data.total || 0;
    }
  } catch (error) {
    console.error('加载数据失败', error);
  }
};

// ---------- 新增/编辑弹窗 ----------
const dialogVisible = ref(false);
const dialogTitle = ref('');
const form = reactive({ id: null, name: '', address: '', contact: '', phone: '', status: 1 });

const openAddDialog = () => {
  dialogTitle.value = '新增仓库';
  Object.assign(form, { id: null, name: '', address: '', contact: '', phone: '', status: 1 });
  dialogVisible.value = true;
};

const openEditDialog = (row) => {
  dialogTitle.value = '编辑仓库';
  Object.assign(form, { ...row });
  dialogVisible.value = true;
};

const submitForm = async () => {
  if (!form.name || form.name.trim() === '') {
    ElMessage.warning('仓库名称不能为空');
    return;
  }
  try {
    let res;
    if (form.id) {
      res = await updateWarehouse(form);
    } else {
      res = await addWarehouse(form);
    }
    if (res.code === 200) {
      ElMessage.success(form.id ? '编辑成功' : '新增成功');
      dialogVisible.value = false;
      loadData();
    } else {
      ElMessage.error(res.msg || '操作失败');
    }
  } catch (error) {
    console.error('提交失败', error);
  }
};

// ---------- 切换状态 ----------
const handleToggleStatus = (row) => {
  const action = row.status === 1 ? '禁用' : '启用';
  ElMessageBox.confirm(`确认要${action}仓库「${row.name}」吗？`, '提示', { type: 'warning' })
      .then(async () => {
        const res = await toggleWarehouseStatus(row.id);
        if (res.code === 200) {
          ElMessage.success(`${action}成功`);
          loadData();
        } else {
          ElMessage.error(res.msg || '操作失败');
        }
      })
      .catch(() => {});
};

// ---------- 删除 ----------
const handleDelete = (id) => {
  ElMessageBox.confirm('确认要删除该仓库吗？', '提示', { type: 'warning' })
      .then(async () => {
        const res = await deleteWarehouse(id);
        if (res.code === 200) {
          ElMessage.success('删除成功');
          loadData();
        } else {
          ElMessage.error(res.msg || '删除失败');
        }
      })
      .catch(() => {});
};

onMounted(() => {
  loadData();
});
</script>