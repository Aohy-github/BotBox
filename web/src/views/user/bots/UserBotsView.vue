<template>
    <div class="container text-center">
        <div class="row">
            <div class="col-3 border">
                <div class="card" style="margin-top: 20px;">
                    <img v-bind:src="$store.state.user.photo" class="card-img-top" alt="..." @error="error_photo">
                    <div class="card-body">
                        <p class="card-text">{{ $store.state.user.username }}</p>
                    </div>
                    
                    </div>
                </div>
            <div class="col-9 border">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-header"> 
                        <span style="font-size: 140%;">
                            My bot
                        </span>
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#addBotModal">add bot</button>
                    </div>

                   <!-- Modal -->
                    <div 
                        class="modal fade " 
                        id="addBotModal" 
                        data-bs-backdrop="static" 
                        data-bs-keyboard="false" 
                        tabindex="-1" 
                        aria-labelledby="staticBackdropLabel" 
                        aria-hidden="true">
                        <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
                            <div class="modal-content">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5" id="staticBackdropLabel">Bot Infomation</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="mb-3">
                                        <label for="recipient-name" class="col-form-label text-start w-100">Recipient</label>
                                        <input type="text" v-model="add_bot_empty.title" class="form-control" id="recipient-name">
                                    </div>
                                    <div class="mb-3">
                                        <label for="description-text" class="col-form-label text-start w-100">Description</label>
                                        <textarea class="form-control" v-model="add_bot_empty.description" id="description-text"></textarea>
                                    </div>
                                    <div class="mb-3">
                                        <label for="content-text" class="col-form-label text-start w-100">Content</label>
                                        <VAceEditor
                                            v-model:value="add_bot_empty.content"
                                            @init="editorInit" lang="c_cpp"
                                            :theme="aceConfig.theme" style="height: 300px"
                                            :options="aceConfig.options" class="ace-editor" />
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <span style="color: red;">{{ add_bot_empty.error_message }}</span>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                <button type="button" class="btn btn-primary" @click="addBot">Submit</button>
                            </div>
                            </div>
                        </div>
                    </div>

                    <div class="card-body">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                <th scope="col">Bot Name</th>
                                <th scope="col">Create Time</th>
                                <th scope="col">Operation</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createTime }}</td>
                                    <td>
                                        <!-- 同理 对应的c触发按钮 也需要绑定 -->
                                        <button 
                                        type="button" 
                                        class="btn btn-primary" 
                                        style="margin-right: 10px;" 
                                        data-bs-toggle="modal" 
                                        data-bs-target="#updateBotModal"
                                        @click="openUpdateModal(bot)"
                                        > 
                                        Modify </button>
                                        <button type="button" class="btn btn-primary"> Delete </button>
                                    </td>
                                </tr>
                            </tbody>
                            </table>
                    </div>
                    <div class="card-footer">
                        <div class="container text-center">
                            <div class="row">
                                <div class="col">
                                    <nav aria-label="Page navigation example">
                                        <ul class="pagination justify-content-center">
                                            <li class="page-item" :class="{ disabled: pageInfo.currentPage == 1 }">
                                                <a class="page-link" href="#" @click.prevent="changePage(pageInfo.currentPage - 1)">Previous</a>
                                            </li>
                                            <li
                                              class="page-item"
                                              v-for="one in pageInfo.indexList"
                                              :key="one"
                                              :class="{ active: pageInfo.currentPage == one }"
                                            >
                                                <a class="page-link" href="#" @click.prevent="changePage(one)">{{ one }}</a>
                                            </li>
                                            <li class="page-item" :class="{ disabled: pageInfo.currentPage == pageInfo.indexList.length }">
                                                <a class="page-link" href="#" @click.prevent="changePage(pageInfo.currentPage + 1)">Next</a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>
                                <div class="col">
                                    <div class="dropdown">
                                        <a class="btn btn-secondary dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                            page Size :{{ pageInfo.pageSize }}
                                        </a>
                                        <ul class="dropdown-menu">
                                            <li><a class="dropdown-item" href="#" @click="changePageSize(5)">5</a></li>
                                            <li><a class="dropdown-item" href="#" @click="changePageSize(10)">10</a></li>
                                            <li><a class="dropdown-item" href="#" @click="changePageSize(20)">20</a></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal -->
        <!-- 每个modal 需要动态加载id  需要 :id 进行绑定 -->
        <div class="modal fade " 
            id="updateBotModal" 
            data-bs-backdrop="static" 
            data-bs-keyboard="false" 
            tabindex="-1" 
            aria-labelledby="updateBotModalLabel" 
            aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
                <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="updateBotModalLabel">Bot Infomation</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body" v-if="currentBot">
                    <form>
                        <div class="mb-3">
                            <label for="recipient-name" class="col-form-label text-start w-100">Title</label>
                            <input type="text" v-model="currentBot.title" class="form-control" id="recipient-name">
                        </div>
                        <div class="mb-3">
                            <label for="description-text" class="col-form-label text-start w-100">Description</label>
                            <textarea class="form-control" v-model="currentBot.description" id="description-text"></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="content-text" class="col-form-label text-start w-100">Content</label>
                            <VAceEditor
                                v-model:value="currentBot.content"
                                
                                @init="editorInit" lang="c_cpp"
                                :theme="aceConfig.theme" style="height: 300px"
                                :options="aceConfig.options" class="ace-editor" />
                            
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <!-- <span style="color: red;">{{ currentBot.error_message }}</span> -->
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" @click="update_bot">Update</button>
                </div>
                </div>
            </div>
        </div>
    </div>
<!-- 
<button @click = "addBot"> 提交 bot</button>
<button @click = "getBotList"> 获取 bot</button>
<button @click = "removeBot"> 删除 bot</button> -->

</template>


<script >
// import CardView from '../../../components/CarView.vue'
import { useStore } from 'vuex'
import { reactive , ref} from 'vue'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import $ from 'jquery'
import { VAceEditor } from 'vue3-ace-editor';  // 代码编辑框的依赖

import 'ace-builds/src-noconflict/ace';
import 'ace-builds/src-noconflict/mode-c_cpp';
import 'ace-builds/src-noconflict/theme-textmate';
import "ace-builds/webpack-resolver";
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-chrome';
import 'ace-builds/src-noconflict/ext-language_tools';

export default{
    components:{
        VAceEditor
    },
    setup(){
        const store = useStore();
        let bots = ref([])
        // 代码编辑框的相应操作
        const aceConfig = reactive({
            theme: 'chrome', //主题
            arr: [
                /*所有主题*/
                "ambiance",
                "chaos",
                "chrome",
                "clouds",
                "clouds_midnight",
                "cobalt",
                "crimson_editor",
                "dawn",
                "dracula",
                "dreamweaver",
                "eclipse",
                "github",
                "gob",
                "gruvbox",
                "idle_fingers",
                "iplastic",
                "katzenmilch",
                "kr_theme",
                "kuroir",
                "merbivore",
                "merbivore_soft",
                "monokai",
                "mono_industrial",
                "pastel_on_dark",
                "solarized_dark",
                "solarized_light",
                "sqlserver",
                "terminal",
                "textmate",
                "tomorrow",
                "tomorrow_night",
                "tomorrow_night_blue",
                "tomorrow_night_bright",
                "tomorrow_night_eighties",
                "twilight",
                "vibrant_ink",
                "xcode",
            ],
            readOnly: false, //是否只读
            options: {
                enableBasicAutocompletion: true,
                enableSnippets: true,
                enableLiveAutocompletion: true,
                tabSize: 2,
                showPrintMargin: false,
                fontSize: 16
            }
        });

        const add_bot_empty = reactive({
            title: "", 
            description: "", 
            content: "", 
            error_message: ""
        })

        const pageInfo = reactive({
            total:10,
            indexList:[],
            currentPage:"1",
            pageSize:5
        })
        
        const currentBot = ref(null);

        const getBotList = ()=>{
            $.ajax({
                url:"http://localhost:3030/user/bot/getlist/",
                type:"get",
                data:{
                    currentPage:pageInfo.currentPage,
                    pageSize:pageInfo.pageSize
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp){
                    pageInfo.indexList=[]
                    bots.value = resp.records
                    pageInfo.total = resp.total
                    for(let index = 1; index < pageInfo.total / pageInfo.pageSize; index ++){
                        pageInfo.indexList.push(index);
                    }
                    //console.log(pageInfo.indexList)
                },error(){
                    console.log("获取失败！")
                }

            })
        }
        getBotList()
        const addBot = () => {
                add_bot_empty.error_message = ""
                $.ajax({
                url: "http://localhost:3030/user/bot/add/",
                type: "post",
                contentType: 'application/json',
                data:JSON.stringify({
                    title : add_bot_empty.title,
                    description : add_bot_empty.description,
                    content : add_bot_empty.content
                }),
                headers:{
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp){
                    if(resp.error_message == "success"){
                        console.log("添加成功");
                        add_bot_empty.title = ""
                        add_bot_empty.description = ""
                        add_bot_empty.content = ""
                        Modal.getInstance("#addBotModal").hide()  // 注意 # 号
                        getBotList();
                    }else{
                        console.log("添加失败")
                        add_bot_empty.error_message = resp.error_message;
                    }
                }
            })

        }
        const removeBot = () =>{
            $.ajax({
                url:"http://localhost:3030/user/bot/add/",
                type:"post",
                contentType: 'application/json',
                data:JSON.stringify({
                    id: "8"
                }),
                headers:{
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(){
                    console.log("remove success");
                },error(){
                    console.log("remove falied");
                }
            })
        }
        const update_bot = ()=>{

        }
        const error_photo = (event) => {
            console.log("触发 error_event")
            // console.log(require('@/assets/images/eva.jpg'));
            event.target.src = require('@/assets/images/eva.jpg');
        }
        const changePageSize= (size) =>{
            pageInfo.pageSize = size;
            getBotList();
        }
        const changePage = (page) => {
            if (page < 1 || page > pageInfo.indexList.length) return;
            pageInfo.currentPage = page;
            getBotList();
        };
        const openUpdateModal = (oneBot) =>{
            currentBot.value = { ...oneBot}
        }
        return {
            addBot,
            getBotList,
            removeBot,
            update_bot,
            error_photo,
            changePageSize,
            changePage,
            openUpdateModal,
            bots,
            add_bot_empty,
            pageInfo,
            currentBot,
            aceConfig
            
        }
    }
}

</script>


<style scoped>
.lable-left{
    text-align: left;
    display: block;
    width: 100%;
}
</style>