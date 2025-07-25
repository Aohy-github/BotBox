import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";

export class Snake extends AcGameObject {
    constructor (info, gamemap) {
        // info 蛇有关的信息
        // gamemap 地图

        // 取id和颜色
        super()
        this.color = info.color 
        this.id = info.id 

        this.gamemap = gamemap

        // 记录蛇身
        this.cells = [new Cell (info.r, info.c)]
        this.next_cell = null  // 记录蛇下一步走什么地方


        // 回合数
        this.step = 0

        // 速度 一帧移动几个格子
        this.speed = 5
        this.direction = -1  // -1 表示没有指令 0 1 2 3表示上 下 左 右
        this.state = 'idle'  // 蛇的状态 idle 表示闲置 moving表示正在移动 die表示死亡

        

        // 行 列 的偏移量
        this.dr = [-1, 0 , 1 ,0]
        this.dc = [0 ,1 ,0 , -1]

        // 判断两个格子是否重合的允许误差
        this.eps = 1e-2

        // 蛇头朝向 为了画眼睛
        this.eye_direction = this.id === 0 ? 0 : 1

        // 两个眼睛的偏移量 对应 上下左右
        this.eye_dx = [  // 蛇眼睛不同方向的x的偏移量
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1],
        ];
        this.eye_dy = [  // 蛇眼睛不同方向的y的偏移量
            [-1, -1],
            [-1, 1],
            [1, 1],
            [1, -1],
        ]


    }

    start () {

    }

   

    check_tail_increasing () { // 检查尾巴是否要增加
        if(this.step <= 10) return true;
        if(this.step % 3 === 1) return true
        return false;
    }
    set_direction(d){
        this.direction = d;

    }
    update () { // 每一帧 都执行一遍 
        if (this.state === 'moving') {
            this.update_move()
        }
        this.render()
    }

    next_step () { // 蛇的状态更新函数
                    // 注意一秒刷新若干次 蛇头的移动是连续的 所以蛇头一直要向next cell移动
        const d = this.direction 
        this.eye_direction = d
        // 根据d对应的方向 判断新的蛇头
        this.next_cell = new Cell (this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d])
        // 清空方向 方向需要用户指定
        this.direction = -1
        this.state = "moving"
        this.eye_direction = d
        this.step ++

        // 将cell所有元素后退一位 把蛇头让出来
        const k = this.cells.length
        for (let i = k; i > 0; i --) {
            // 注意 js事直接可以索引到[n]来添加元素的
            // 注意使用deepcopy
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]))
        }

        // 检查 如果nextcell要撞了 就改变状态
        // 牢记这个游戏的时间有两个粒度 宏观（要去哪）和微观（每一帧的更新过程）

        if (!this.gamemap.check_valid(this.next_cell)) this.state = 'die'
    }
    update_move () {

        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy)
        
        if (distance < this.eps) { // 到了目的地
            this.cells[0] = this.next_cell // 添加一个新蛇头
            this.next_cell = null 
            this.state = "idle"

            if(!this.check_tail_increasing()) // 蛇不变长
                this.cells.pop();
        }else{
            // 否则 接着移动 根据几何关系推断
            const move_distance = this.speed * this.timedelta / 1000; // 两帧之间的距离
            this.cells[0].x += move_distance * dx / distance 
            this.cells[0].y += move_distance * dy / distance

            if (!this.check_tail_increasing()) {
                const k = this.cells.length;
                const tail = this.cells[k - 1], tail_target = this.cells[k - 2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }      
        }
        
    } 


    render () {
        const L = this.gamemap.L
        const ctx = this.gamemap.ctx

        ctx.fillStyle = this.color

        // 如果蛇去世 变颜色
        if (this.state === 'die') {
            ctx.fillStyle = 'white'
        }
        
        // 枚举每一个身体
        for (const cell of this.cells) {
            // 画圆
            ctx.beginPath()
            ctx.arc(cell.x * L, cell.y * L, L / 2 * 0.8, 0, 2 * Math.PI)  // 圆心x 圆心y 半径 起始弧度 终止弧度
            ctx.fill()
        }

        // // 为了让蛇线条流畅 在圆圈之间两两画一个矩形
        for (const cell of this.cells) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L / 2 * 0.8, 0, Math.PI * 2);
            ctx.fill();
        }

        for (let i = 1; i < this.cells.length; i ++ ) {
            const a = this.cells[i - 1], b = this.cells[i];
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps)
                continue;
            if (Math.abs(a.x - b.x) < this.eps) {
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }


        // // 画眼睛
        ctx.fillStyle = "black";
        for (let i = 0; i < 2; i ++ ) {
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;

            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }

    }
}