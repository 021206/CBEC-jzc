<template>
  <div>
    <el-page-header @back="goBack" content="出库单详情" style="margin-bottom: 20px;" />

    <el-card shadow="never" style="margin-bottom: 20px;">
      <template #header>
        <span style="font-weight: bold;">基本信息</span>
      </template>
      <el-descriptions :column="4" border>
        <el-descriptions-item label="出库单号">{{ detail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusMap[detail.status]?.type || 'info'">
            {{ statusMap[detail.status]?.label || '未知' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="仓库">{{ detail.warehouseName }}</el-descriptions-item>
        <el-descriptions-item label="出库类型">
          {{ detail.outboundType === 1 ? '订单发货' : '调拨出库' }}
        </el-descriptions-item>
        <el-descriptions-item label="审核人">{{ detail.auditor || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审核时间">{{ detail.auditTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detail.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detail.createBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card shadow="never">
      <template #header>
        <span style="font-weight: bold;">出库明细</span>
      </template>
      <el-table :data="detail.items || []" border stripe>
        <el-table-column prop="productId" label="商品ID" width="100" />
        <el-table-column prop="plannedQty" label="计划数量" width="120" />
        <el-table-column prop="actualQty" label="实际数量" width="120" />
      </el-table>
    </el-card>

    <div style="margin-top: 20px; display: flex; gap: 10px;">
      <el-button v-if="detail.status === 1" type="warning" @click="handleSubmit">提交审核</el-button>
      <el-button v-if="detail.status === 2" type="success" @click="handleApprove">审核通过</el-button>
      <el-button v-if="detail.status === 2" type="danger" @click="handleReject">驳回</el-button>
      <el-button @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getOutboundOrderDetail, submitOutboundAudit, approveOutboundOrder, rejectOutboundOrder } from '@/api/outbound.js';

const route = useRoute();
const router = useRouter();

const statusMap = {
  1: { label: '草稿', type: 'info' },
  2: { label: '待审核', type: 'warning' },
  3: { label: '已完成', type: 'success' },
  4: { label: '驳回', type: 'danger' }
};

const detail = ref({});

const loadDetail = async () => {
  const id = route.params.id;
  const res = await getOutboundOrderDetail(id);
  if (res.code === 200) {
    detail.value = res.data;
  }
};

const goBack = () => {
  router.push('/inventory/outbound');
};

const handleSubmit = async () => {
  try {
    await ElMessageBox.confirm('确认提交审核？', '提示', { type: 'warning' });
    const res = await submitOutboundAudit(detail.value.id);
    if (res.code === 200) {
      ElMessage.success('提交审核成功');
      loadDetail();
    }
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('操作失败');
  }
};

const handleApprove = async () => {
  try {
    await ElMessageBox.confirm('确认审核通过？', '提示', { type: 'warning' });
    const res = await approveOutboundOrder(detail.value.id, 'admin');
    if (res.code === 200) {
      ElMessage.success('审核通过');
      loadDetail();
    }
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('操作失败');
  }
};

const handleReject = async () => {
  try {
    await ElMessageBox.confirm('确认驳回？', '提示', { type: 'warning' });
    const res = await rejectOutboundOrder(detail.value.id, 'admin');
    if (res.code === 200) {
      ElMessage.success('已驳回');
      loadDetail();
    }
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('操作失败');
  }
};

onMounted(loadDetail);
</script>