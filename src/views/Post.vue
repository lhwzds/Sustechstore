<template>
  <div class="post-main">
    <el-backtop></el-backtop>

    <HeaderBar></HeaderBar>

    <el-card id="post-card">
        <el-row type="flex" justify="center">
            <h3>商品信息填写</h3>
        </el-row>
        
        <el-row type="flex" justify="center">
            <el-col :span="16">
                <el-form ref="form" :model="form" label-width='auto' label-position="top">
                    <el-form-item class="form-item" label="商品名称（至多40字符）" required>
                        <el-input maxlength="40" v-model="form.name" style="width:75%"></el-input>
                    </el-form-item>
                
                    <el-form-item class="form-item" label="商品图片（最多可上传6张）" required :label-width="formLabelWidth" >
                        <el-upload 
                        ref="elUpload" 
                        action="#" 
                        accept="image/png,image/gif,image/jpg,image/jpeg" 
                        list-type="picture-card"
                        :limit=limitNum
                        :auto-upload="false"
                        :on-preview="handlePictureCardPreview"
                        :http-request="uploadImage"
                        :before-upload="beforeUpload"
                        >
                            <i class="el-icon-plus"></i>
                        </el-upload>
                        <el-dialog :visible.sync="dialogVisible">
                            <img width="100%" :src="dialogImageUrl" alt="">
                        </el-dialog>
                    </el-form-item>

                    <el-form-item class="form-item" label="商品数量" required>
                        <el-input v-model="form.quantity" style="width:75%"></el-input>
                    </el-form-item>

                    <el-form-item class="form-item" label="商品价格" required>
                        <el-input v-model="form.price" style="width:75%"></el-input>
                    </el-form-item>

                    <el-form-item class="form-item" label="商品简介（至多150字符）">
                        <el-input maxlength="150" type="textarea" autosize :rows="2" placeholder="请输入内容" v-model="form.text"></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button size="small" type="primary" @click="uploadFile">立即上传</el-button>
                        <el-button size="small">取消</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>

    <FootBar></FootBar>
  </div>
</template>

<script>
import HeaderBar from '../components/HeaderBar.vue'
import FootBar from '../components/FootBar.vue'
import axios from 'axios'

    export default {
        data() {
            return {
                form: {
                    name: '',
                    quantity: '',
                    text: '',
                    price: '',
                },
                dialogImageUrl: '',
                dialogVisible: false,
                formLabelWidth: '80px',
                limitNum: 6,
                upFile: new FormData(),
            };
        },
        components: {
          HeaderBar,
          FootBar
        },
        methods:{
            setSize() {
                // 通过浏览器宽度(图片宽度)计算高度
                this.bannerHeight = 400 / 1920 * this.screenWidth;
            },
            
            // 点击文件列表中已上传的文件时的钩子
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },

            beforeUpload(file) {
                if (file) {
                    console.log(file)
                    this.upFile.append('picture', file)
                }
                else {
                    return false
                } 
            },

            checkUpload() {
                if (this.form.name == '' || this.form.price == null){
                    alert('商品名称不能为空')
                    return false
                }
                if (this.form.price == '' || this.form.price == null){
                    alert('商品价格不能为空')
                    return false
                }
                else{
                    if (!(/(^[1-9]\d*$)/.test(this.form.price))){
                        alert('商品价格请输入正整数')
                        return false
                    }
                }
                if (this.form.quantity == '' || this.form.quantity == null){
                    alert('商品数量不能为空')
                    return false
                }
                else{
                    if (!(/(^[1-9]\d*$)/.test(this.form.quantity))){
                        alert('商品数量请输入正整数')
                        return false
                    }
                }
                return true
            },

            uploadImage() {},

            uploadFile() {
                if (this.checkUpload() == true) {
                    let fileList = this.upFile
                    fileList.append('merchandiseName', this.form.name)
                    fileList.append('merchandiseQuantity', this.form.quantity)
                    fileList.append('merchandisePrice', this.form.price)
                    fileList.append('merchandiseText', this.form.text)
                    this.$refs.elUpload.submit() 
                    axios({
                        url:"/root"+"/items/add",
                        method:'post',
                        data:fileList,
                        headers:{
                            'Content-Type':'multipart/form-data'
                        }
                    }).then(res =>{
                        console.log(res)
                        alert("上传成功!")})
                    .catch(function(err){
                        console.log(err)
                        alert("上传失败")
                    })
                    this.upFile = new FormData()
                }
            },
        },
        mounted() {
          // 首次加载时,需要调用一次
          this.screenWidth =  window.innerWidth;
          this.setSize();
          // 窗口大小发生改变时,调用一次
          window.onresize = () =>{
            this.screenWidth =  window.innerWidth;
            this.setSize();
          }
        }
    }
</script>

<style>
    img {
        /*设置图片宽度和浏览器宽度一致*/
        width:100%;
        height:inherit;
    }
    #post-card {
        width: 75%;
        border-radius: 15px;
        margin: auto;
        margin-top: 20px;
    }
    .form-item {
        text-align: left;
    }
    
</style>