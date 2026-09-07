<template>
  <div>
    <el-page-header @back="$router.back()" content="创建入库单" style="margin-bottom: 20px;" />

    <el-form :model="form" label-width="100px" style="max-width: 600px;">
      <el-form-item label="仓库" required>
        <el-select v-model="form.warehouseId" placeholder="请选择仓库" style="width: 100%;">
          <el-option
              v-for="item in warehouseList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="供应商">
        <el-input v-model="form.supplier" placeholder="请输入供应商" />
      </el-form-item>
      <el-form-item label="入库类型">
        <el-radio-group v-model="form.inboundType">
          <el-radio :label="1">采购入库</el-radio>
          <el-radio :label="2">退货入库</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
      </el-form-item>
    </el-form>

    <el-divider>入库明细</el-divider>

    <div style="margin-bottom: 10px;">
      <el-button type="primary" size="small" @click="addItem">添加商品</el-button>
    </div>

    <el-table :data="items" border stripe>
      <el-table-column label="商品" width="200">
        <template #default="{ row, $index }">
          <el-select v-model="row.productId" placeholder="请选择商品" style="width: 100%;">
            <el-option
                v-for="item in productList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="计划数量" width="150">
        <template #default="{ row }">
          <el-input-number v-model="row.plannedQty" :min="1" style="width: 100%;" />
        </template>
      </el-table-column>
      <el-table-column label="实际数量" width="150">
        <template #default="{ row }">
          <el-input-number v-model="row.actualQty" :min="1" style="width: 100%;" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ $index }">
          <el-button size="small" type="danger" @click="removeItem($index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top: 20px; display: flex; gap: 10px;">
      <el-button type="primary" @click="handleSubmit">保存</el-button>
      <el-button @click="$router.back()">取消</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { getAllProducts } from '@/api/product.js';
import { getAllWarehouses } from '@/api/warehouse.js';
import { createInboundOrder } from '@/api/inbound.js';

const router = useRouter();

const productList = ref([]);
const warehouseList = ref([]);

const form = reactive({
  warehouseId: null,
  supplier: '',
  inboundType: 1,
  remark: ''
});

const items = ref([]);

const loadOptions = async () => {
  const [products, warehouses] = await Promise.all([
    getAllProducts(),
    getAllWarehouses()
  ]);
  if (products.code === 200) productList.value = products.data || [];
  if (warehouses.code === 200) warehouseList.value = warehouses.data || [];
};

const addItem = () => {
  items.value.push({ productId: null, plannedQty: 1, actualQty: 1 });
};

const removeItem = (index) => {
  items.value.splice(index, 1);
};

const handleSubmit = async () => {
  if (!form.warehouseId) {
    ElMessage.warning('请选择仓库');
    return;
  }
  if (items.value.length === 0) {
    ElMessage.warning('请至少添加一个商品');
    return;
  }
  for (const item of items.value) {
    if (!item.productId) {
      ElMessage.warning('请选择商品');
      return;
    }
    if (!item.plannedQty || item.plannedQty <= 0) {
      ElMessage.warning('计划数量必须大于0');
      return;
    }
  }
  // 自动填充 actualQty = plannedQty
  const data = {
    order: form,
    items: items.value.map(item => ({
      productId: item.productId,
      plannedQty: item.plannedQty,
      actualQty: item.actualQty || item.plannedQty
    }))
  };
  const res = await createInboundOrder(data);
  if (res.code === 200) {
    ElMessage.success('入库单创建成功');
    router.push('/inventory/inbound');
  } else {
    ElMessage.error(res.msg || '创建失败');
  }
};

onMounted(() => {
  loadOptions();
  addItem(); // 默认添加一行
});
</script>