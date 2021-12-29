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
                <el-row style="margin-top:20px;margin-bottom:20px;text-align:left;">
                    服务类型: 
                    <el-radio v-model="radio_type" label="1" style="margin-left:20px;">商品</el-radio>
                    <el-radio v-model="radio_type" label="2">跑腿</el-radio> 
                    <el-radio v-model="radio_type" label="3">商品求购</el-radio>
                </el-row>
                
                <el-form ref="form" :model="form" label-width='auto' label-position="top" v-if="radio_type=='1'">
                    <el-form-item class="form-item" label="商品名称（至多40字符）" required>
                        <el-input maxlength="40" v-model="form.name" style="width:75%"></el-input>
                    </el-form-item>
                
                    <el-form-item class="form-item" label="商品图片（最多可上传3张）" required :label-width="formLabelWidth" >
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

                    <el-form-item class="form-item" label="商品标签" required>
                        <el-tag :key="tag" style="margin-right:10px" v-for="tag in dynamicTags" closable :disable-transitions="false" @close="handleClose(tag)">
                            {{tag}}
                        </el-tag> 
                        <el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput" size="small"
                         @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
                        </el-input>
                        <el-button v-else class="button-new-tag" size="small" @click="showInput">+ 添加标签</el-button>
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

                 <el-form ref="form" :model="form" label-width='auto' label-position="top" v-if="radio_type=='2'">
                     <el-form-item class="form-item" label="跑腿名称（至多40字符）" required>
                        <el-input maxlength="40" v-model="form.name" style="width:75%"></el-input>
                    </el-form-item>

                    <el-form-item class="form-item" label="跑腿价格" required>
                        <el-input v-model="form.price" style="width:75%"></el-input>
                    </el-form-item>

                    <el-form-item class="form-item" label="跑腿简介（至多150字符）">
                        <el-input maxlength="150" type="textarea" autosize :rows="2" placeholder="请输入内容" v-model="form.text"></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button size="small" type="primary" @click="uploadFile">立即上传</el-button>
                        <el-button size="small">取消</el-button>
                    </el-form-item>
                </el-form>

                <el-form ref="form" :model="form" label-width='auto' label-position="top" v-if="radio_type=='3'">
                     <el-form-item class="form-item" label="求购商品名称（至多40字符）" required>
                        <el-input maxlength="40" v-model="form.name" style="width:75%"></el-input>
                    </el-form-item>

                    <el-form-item class="form-item" label="预期价格" required>
                        <el-input v-model="form.price" style="width:75%"></el-input>
                    </el-form-item>

                    <el-form-item class="form-item" label="相关简介（至多150字符）">
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
                    text: '',
                    price: '',
                },
                radio_type: "1",
                dialogImageUrl: '',
                dialogVisible: false,
                formLabelWidth: '80px',
                limitNum: 3,
                upFile: new FormData(),
                dynamicTags: [],
                inputVisible: false,
                inputValue: ''
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
                    alert('名称不能为空')
                    return false
                }
                if (this.form.price == '' || this.form.price == null){
                    alert('价格不能为空')
                    return false
                }
                else{
                    if (!(/^[0-9]+(\.\d+)?$/.test(this.form.price))){
                        alert('价格请输入正数')
                        return false
                    }
                }
                return true
            },

            uploadImage() {},

            uploadFile() {
                if (this.checkUpload() == true) {
                    let fileList = this.upFile
                    fileList.append('merchandiseType', this.radio_type)
                    fileList.append('merchandisePrice', this.form.price)
                    fileList.append('merchandiseText', this.form.text)
                    if(this.radio_type == "1"){
                        fileList.append('merchandiseTags', this.dynamicTags)
                        fileList.append('merchandiseName', this.form.name)
                        this.$refs.elUpload.submit() 
                    }
                    else if(this.radio_type == '2'){
                        fileList.append('merchandiseName', '跑腿: '+this.form.name)
                    }
                    else{
                        fileList.append('merchandiseName', '求购: '+this.form.name)
                    }
                    axios({
                        url:"/root"+"/items/add",
                        method:'post',
                        data:fileList,
                        headers:{
                            'Content-Type':'multipart/form-data'
                        }
                    }).then(res =>{
                        console.log(res)
                        this.$confirm("上传成功!", '提示', {
                            confirmButtonText: '确定',
                            type: 'success'})
                        .then(this.$router.push('/'))
                        })
                    .catch(function(err){
                        console.log(err)
                        alert("上传失败")
                    })
                    this.upFile = new FormData()
                    
                }
            },

            handleClose(tag) {
                this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
            },

            showInput() {
                this.inputVisible = true;
                this.$nextTick(_ => {this.$refs.saveTagInput.$refs.input.focus();console.log(_)});
            },

            handleInputConfirm() {
                let inputValue = this.inputValue;
                if (inputValue) {
                    this.dynamicTags.push(inputValue);
                }
                this.inputVisible = false;
                this.inputValue = '';
            }
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
    el-tag + .el-tag {
        margin-left: 10px;
    }
    .button-new-tag {
        height: 32px;
        line-height: 30px;
        padding-top: 0;
        padding-bottom: 0;
    }
    .input-new-tag {
        width: 90px;
        vertical-align: bottom;
    }
    
</style>