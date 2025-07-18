import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";


export class Snake extends AcGameObject{
    constructor(info, gamemap){
        super();


        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;


        this.cells = [new Cell(info.r , info.c)];

        this.speed = 5;
    }

    start(){

    }
    update_move(){
        this.cells[0].x += this.speed * this.timedelta /1000
    }
    update(){
        this.render();
        //this.update_move();
    }

    render(){
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        for(const cell of this.cells){
            ctx.beginPath();
            ctx.arc(cell.x * L , cell.y * L , L / 2 , 0 , Math.PI * 2);
            ctx.fill();
        }
    }
}