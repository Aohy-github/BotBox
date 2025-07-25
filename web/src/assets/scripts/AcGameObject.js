const AC_GAME_OBJECTES = [];


export class AcGameObject{
    constructor(){
        AC_GAME_OBJECTES.push(this);
        this.timedelta = 0;
        this.has_called_start = false;
    }

    // 第一帧
    start(){

    }

    // 第一帧之后
    update(){

    }

    on_destroy(){

    }

    destroy(){
        for(let i in AC_GAME_OBJECTES){
            const obj = AC_GAME_OBJECTES[i];
            if(obj === this){
                AC_GAME_OBJECTES.splice(i, 1);
                break;
            }
        }
    }
}

let last_timestamp;

const step = timestamp => {
    //of 遍历值，in 遍历键/索引
    for(let obj of AC_GAME_OBJECTES){
        if(!obj.has_called_start){
            obj.has_called_start = true;
            obj.start();
        }else{
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }

    last_timestamp = timestamp;
    requestAnimationFrame(step);
}

requestAnimationFrame(step)