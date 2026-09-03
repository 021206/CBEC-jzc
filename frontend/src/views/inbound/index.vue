<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h2>入库单管理</h2>
      <el-button type="primary" @click="$router.push('/inbound/create')">新建入库单</el-button>
    </div>

    <!-- 搜索/筛选 -->
    <el-form :inline="true" :model="searchForm">
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="全部状态" clearable>
          <el-option label="草稿" :value="1" />
          <el-option label="待审核" :value="2" />
          <el-option label="已完成" :value="3" />
          <el-option label="驳回" :value="4" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="orderNo" label="入库单号" width="180" />
      <el-table-column prop="warehouseId" label="仓库ID" width="100" />
      <el-table-column prop="supplier" label="供应商" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status].type">
            {{ statusMap[row.status].label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="auditor" label="审核人" width="120" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="viewDetail(row.id)">查看</el-button>
          <el-button
              v-if="row.status === 1"
              size="small"
              type="warning"
              @click="handleSubmit(row.id)"
          >
            提交审核
          </el-button>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { getInboundOrderList, submitInboundAudit } from '@/api/inbound.js';

const router = useRouter();

const statusMap = {
  1: { label: '草稿', type: 'info' },
  2: { label: '待审核', type: 'warning' },
  3: { label: '已完成', type: 'success' },
  4: { label: '驳回', type: 'danger' }
};

const searchForm = reactive({ status: null });
const tableData = ref([]);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);

const loadData = async () => {
  const res = await getInboundOrderList();
  if (res.code === 200) {
    let list = res.data || [];
    if (searchForm.status) {
      list = list.filter(item => item.status === searchForm.status);
    }
    tableData.value = list;
    total.value = list.length;
  }
};

const resetSearch = () => {
  searchForm.status = null;
  pageNum.value = 1;
  loadData();
};

const viewDetail = (id) => {
  router.push(`/inbound/detail/${id}`);
};

const handleSubmit = async (id) => {
  try {
    const res = await submitInboundAudit(id);
    if (res.code === 200) {
      ElMessage.success('提交审核成功');
      loadData();
    }
  } catch (e) {
    ElMessage.error('提交审核失败');
  }
};

onMounted(loadData);
</script>