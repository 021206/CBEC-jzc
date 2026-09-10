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

    <!-- 趋势图 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <template #header>
        <span style="font-weight: bold;">近7天出入库趋势</span>
      </template>
      <div ref="chartRef" style="width: 100%; height: 350px;"></div>
    </el-card>

    <!-- 预警列表 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <template #header>
        <span style="font-weight: bold; color: #F56C6C;">⚠️ 库存预警</span>
      </template>
      <el-table :data="stats.warningList || []" border stripe>
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="warehouseName" label="仓库" />
        <el-table-column prop="quantity" label="当前库存" width="120">
          <template #default="{ row }">
            <el-tag type="danger">{{ row.quantity }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warningThreshold" label="预警阈值" width="120" />
      </el-table>
      <div v-if="!stats.warningList || stats.warningList.length === 0" style="text-align: center; color: #999; padding: 20px;">
        ✅ 暂无预警商品
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue';
import { ElMessage } from 'element-plus';
import * as echarts from 'echarts';
import { getStatisticsOverview } from '@/api/statistics.js';

const stats = ref({});
const chartRef = ref(null);
let chartInstance = null;

const loadData = async () => {
  try {
    const res = await getStatisticsOverview();
    if (res.code === 200) {
      stats.value = res.data || {};
      // 等 DOM 渲染完成后绘制图表
      await nextTick();
      renderChart();
    } else {
      ElMessage.error(res.msg || '加载统计数据失败');
    }
  } catch (error) {
    console.error('加载统计数据失败', error);
    ElMessage.error('网络异常，请稍后重试');
  }
};

const renderChart = () => {
  if (!chartRef.value) return;
  if (chartInstance) {
    chartInstance.dispose();
  }
  chartInstance = echarts.init(chartRef.value);

  const trendData = stats.value.trendData || [];
  const dates = trendData.map(d => d.date);
  const inboundData = trendData.map(d => d.inbound);
  const outboundData = trendData.map(d => d.outbound);

  chartInstance.setOption({
    tooltip: { trigger: 'axis' },
    legend: {
      data: ['入库', '出库'],
      bottom: 0,          // 图例固定在底部
      itemGap: 30         // 两个图例之间的间距
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '60px',     // 底部留出 60px 给图例
      top: '40px',        // 顶部留点空间
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      boundaryGap: false  // 折线从 y 轴起点开始
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '入库',
        type: 'line',
        smooth: true,
        data: inboundData,
        itemStyle: {color: '#67C23A'},
        areaStyle: {color: 'rgba(103, 194, 58, 0.1)'}
      },
      {
        name: '出库',
        type: 'line',
        smooth: true,
        data: outboundData,
        itemStyle: {color: '#F56C6C'},
        areaStyle: {color: 'rgba(245, 108, 108, 0.1)'}
      }
    ]
  });

  // 响应窗口大小变化
  window.addEventListener('resize', () => {
    chartInstance && chartInstance.resize();
  });
};

onMounted(loadData);
</script>