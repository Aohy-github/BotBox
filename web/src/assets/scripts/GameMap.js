import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";
import { Snake } from "./Snake";
export class GameMap extends AcGameObject{
    constructor(ctx , parent , store){
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;
        this.cols = 13;
        this.rows = 13;
        this.store = store
        this.inner_walls_count = 20;
        this.walls = [];
        this.keys_listening = false;

        this.snakes = [
            new Snake({id: 0 , color:"#ff0101ff" , r:this.rows - 2, c: 1},this),
            new Snake({id: 1 , color:"#372c74" , r:1, c: this.cols - 2},this),
        ];
    }
    create_walls_front(){
        const g = [];
        for(let r = 0; r < this.rows ; r++){
            g[r] = [];
            for(let c = 0; c < this.cols ; c++){
                g[r][c] = false;
            }
        }

        // 创建左右边界
        for(let r = 0 ; r < this.rows ; r++){
            g[r][0] = g[r][this.cols - 1] = true;
        }

        // 创建上下边界
        for(let c = 0 ; c < this.cols ; c++){
            g[0][c] = g[this.rows - 1][c] = true;
        }

        // 创建随机障碍物
        for(let i = 0; i < this.inner_walls_count / 2 ; i++){
            for(let j = 0; j < 100; j++){
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);

                if(g[r][c] || g[this.rows - 1 - r][this.cols - 1- c]) continue
                
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) continue;
                
                g[r][c] = g[this.rows - 1 - r][this.cols - 1- c] = true;
                break;
            }
        }
        
        // 返回生成的地图
        return g;
    }
    create_walls(){
        let g = this.store.state.pk.gamemap;
        if(g === null){
            g = this.create_walls_front();
        }
        // console.log(g);
        for(let r = 0; r < this.rows ; r++){
            for(let c = 0; c < this.cols ;c++){
                if(g[r][c]){
                    this.walls.push(new Wall(r,c, this));
                }
            }
        }
        return true;
    }
    start(){
        this.create_walls()
        this.add_listening()
    }

    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols
        this.ctx.canvas.height = this.L * this.rows
    }
    next_step () {
        // 进入下一回合的函数
        for (const snake of this.snakes) {
            snake.next_step()  // 更新蛇位置和状态
        }
    }

    check_valid(cell) {  // 检测目标位置是否合法：没有撞到两条蛇的身体和障碍物
        for (const wall of this.walls) {
            if (wall.r === cell.r && wall.c === cell.c)
                return false;
        }

        for (const snake of this.snakes) {
            let k = snake.cells.length;
            if (!snake.check_tail_increasing()) {  // 当蛇尾会前进的时候，蛇尾不要判断
                k -- ;
            }
            for (let i = 0; i < k; i ++ ) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c)
                    return false;
            }
        }

        return true;
    }

    update(){
        this.update_size();
        
        // 异步更新：不需要等待所有蛇都有方向才移动
        for(const snake of this.snakes){
            if(snake.direction >= 0){
                snake.next_step();
            }
            // 每帧都更新蛇的状态（移动动画等）
            snake.update();
        }
        
        this.render();
    }

    // 监听用户输入
    add_listening (){
        if (this.keys_listening) return;
        this.keys_listening = true;
        
        this.ctx.canvas.focus();
        
        // const [snake0 , snake1] = this.snakes;
        this.ctx.canvas.addEventListener("keydown" , e =>{
            e.preventDefault();
            console.log("e.key :" + e.key)
            let d = -1;
            // 本地立即响应用户输入
            if(e.key === 'w' || e.key === 'W') d = 0;
            else if(e.key === 'd' || e.key === 'D') d = 1;
            else if(e.key === 's' || e.key === 'S') d = 2;
            else if(e.key === 'a' || e.key === 'A') d = 3;
            
            if(d >= 0){
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "move",
                    direction:d,
                }))
            }
        })
    }
    check_ready () {
        // 看看两条蛇是否都准备好了 进行下一个回合
        // 注意 用户的操作必须都放到后端进行计算

        for (const snake of this.snakes) {
            if (snake.state !== 'idle') return false;
            if (snake.direction === -1) return false;
        }
        return true
    }


    render(){
        const color_even = "#AAD751" , color_odd = "#435e0fff";
        for(let r = 0; r < this.rows; r ++){
            for(let c = 0; c < this.cols; c ++){
                if((r + c) % 2 == 0){
                    this.ctx.fillStyle = color_even;
                }else{
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(this.L * r , this.L * c, this.L , this.L);
            }
            
        }
    }
}
