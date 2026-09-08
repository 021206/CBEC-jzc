<template>
  <div>
    <h2 style="margin-bottom: 20px;">数据概览</h2>

    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center;">
            <div style="font-size: 14px; color: #909399;">商品总数</div>
            <div style="font-size: 32px; font-weight: bold; color: #409EFF;">{{ stats.totalProducts || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center;">
            <div style="font-size: 14px; color: #909399;">仓库总数</div>
            <div style="font-size: 32px; font-weight: bold; color: #67C23A;">{{ stats.totalWarehouses || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center;">
            <div style="font-size: 14px; color: #909399;">今日入库</div>
            <div style="font-size: 32px; font-weight: bold; color: #E6A23C;">{{ stats.todayInboundQty || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center;">
            <div style="font-size: 14px; color: #909399;">今日出库</div>
            <div style="font-size: 32px; font-weight: bold; color: #F56C6C;">{{ stats.todayOutboundQty || 0 }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 预留趋势图位置 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <template #header>
        <span style="font-weight: bold;">近7天出入库趋势</span>
      </template>
      <div style="text-align: center; color: #999; padding: 40px 0;">
        趋势图开发中...
      </div>
    </el-card>

    <!-- 预留预警列表位置 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <template #header>
        <span style="font-weight: bold;">库存预警</span>
      </template>
      <div style="text-align: center; color: #999; padding: 20px 0;">
        暂无预警商品
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getStatisticsOverview } from '@/api/statistics.js';

const stats = ref({});

const loadData = async () => {
  try {
    const res = await getStatisticsOverview();
    if (res.code === 200) {
      stats.value = res.data || {};
    } else {
      ElMessage.error(res.msg || '加载统计数据失败');
    }
  } catch (error) {
    console.error('加载统计数据失败', error);
    ElMessage.error('网络异常，请稍后重试');
  }
};

onMounted(loadData);
</script>