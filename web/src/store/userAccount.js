import $ from 'jquery'

export default{
    state:{
        id:"",
        username:"",
        photo:"",
        token:"",
        is_login:false,
    },
    getters:{},
    mutations:{
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state, token) {
            state.token = token;
        },
        logout(state){
            state.id = "",
            state.username = "",
            state.photo = "",
            state.token = "",
            state.is_login = false
        }

    },
    actions:{
        login(context, data){
            $.ajax({
                url :"http://localhost:3030/user/account/token/",
                type : "post",
                contentType: 'application/json',
                data:JSON.stringify({
                    username: data.username,
                    password: data.password
                }),
                success(resp){
                    console.log("user.js 登陆成功！")
                    if(resp.error_message === "success"){
                        localStorage.setItem("jwt_token" , resp.token);
                        context.commit("updateToken",resp.token);
                        data.success();
                    }else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
            });
        },
        getInfo(context, data){
            console.log("token :" + context.state.token)
            $.ajax({
                url: "http://localhost:3030/user/account/info/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp){
                    if(resp.error_message === "success"){
                        context.commit("updateUser" ,{
                            ...resp,
                            is_login: true
                        });
                        data.success();
                    }else{
                        data.error(resp);
                    }
                },
                error(resp){
                    data.error(resp);
                }
            });
        },
        logout(context){
            localStorage.removeItem("jwt_token");
            context.commit("logout")
        },
        
    },
    modules:{}
}   