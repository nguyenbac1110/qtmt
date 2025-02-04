import { Component, OnInit, ViewChild } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { SelectModule } from 'primeng/select';
import { ChartModule, UIChart } from 'primeng/chart';
import { HttpService } from "../../services/http.service";
import { ProgressSpinnerModule } from "primeng/progressspinner";
import { BlockUIModule } from "primeng/blockui";
import { HttpClientModule } from "@angular/common/http";
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';

@Component({
    standalone: true,
    selector: "telemetry-device",
    templateUrl: "./telemetry.component.html",
    providers: [HttpService, MessageService],
    imports: [HttpClientModule,SelectModule,FormsModule, ReactiveFormsModule,ChartModule, BlockUIModule, ProgressSpinnerModule, ToastModule,ButtonModule]
})
export class TelemetryDevice implements OnInit{
    devices: Array<any> = [];
    deviceSelected: any = null;
    dataTelemetry: Array<any> = [];
    dataChart: any = [];
    options: any = {};
    blockedDocument: boolean = false;
    @ViewChild("chart") chart: UIChart;
    constructor(private httpService: HttpService, private messageService: MessageService){

    }

    ngOnInit(): void {
        this.getAllDevice();
        this.createOptions();
    }

    getAllDevice(){
        let me = this;
        me.blockedDocument = true;
        this.httpService.get("get-all-device", {}, {page: 0, size: 99999}, (response: any)=>{
        //this.httpService.get("/data/devices.json", {}, {page: 0, size: 99999}, (response: any)=>{
            this.devices = response.content;
            me.blockedDocument = false;
        }, ()=>{
            me.messageService.add({ severity: 'error', summary: 'Lỗi', detail: 'Tải danh sách thiết bị thất bại' });
        }, ()=>{
            me.blockedDocument = false;
        })
    }

    changeDevice(event:any){
        this.deviceSelected = event;
        this.loadDataForDevice();
    }

    reload(){
        if(!this.deviceSelected){
            this.messageService.add({ severity: 'error', summary: 'Lỗi', detail: 'Vui lòng chọn thiết bị' });
            return;
        }
        this.loadDataForDevice();
    }

    loadDataForDevice(){
        let me = this;
        this.blockedDocument = true;
        this.httpService.get("get-all-cin-by-device/"+this.deviceSelected.id, {}, {}, (response: any)=>{
        //this.httpService.get("/data/telemetry.json", {}, {}, (response: any)=>{
            me.dataTelemetry = response;
            me.dataChart = {
                labels: this.dataTelemetry.map(el => this.formatDatetime(el.time)),
                datasets: [
                    {
                        label: "Độ ẩm",
                        data: this.dataTelemetry.map(el => el.humi),
                        fill: false,
                        tension: 0.4,
                        yAxisID: 'y',
                    },
                    {
                        label: "Nhiệt độ",
                        data: this.dataTelemetry.map(el => el.temp),
                        fill: false,
                        tension: 0.4,
                        yAxisID: 'y1',
                    }
                ]
            }
            me.chart.reinit();
            me.blockedDocument = false;
        }, ()=>{
            me.messageService.add({ severity: 'error', summary: 'Lỗi', detail: 'Tải telemetry thiết bị thất bại' });
        }, ()=>{
            me.blockedDocument = false;
        })
    }

    createOptions(){
        this.options = {
            maintainAspectRatio: false,
            aspectRatio: 0.6,
            plugins: {
                title: {
                    display: true,
                    text: "Biểu đồ dữ liệu quan trắc",
                    font: {
                        size: 16,
                        weight: "bold",
                        family: "Cambria, Cochin, Georgia, Times, 'Times New Roman', serif"
                    },
                    color: "black"
                }
            },
            scales: {
                y: {
                    title: {
                        text: "%",
                        display: true
                    },
                    beginAtZero: true,
                    type: "linear",
                    display: true,
                    position: "left"
                },
                y1: {
                    title: {
                        text: "°C",
                        display: true
                    },
                    beginAtZero: true,
                    type: "linear",
                    display: true,
                    position: "right"
                },
                x: {
                    title: {
                        text: "Thời gian",
                        display: true
                    }
                }
            }
        };
    }

    formatDatetime(datetime: Date){
        if(!datetime) return "";
        let date = new Date(datetime);
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let day = date.getDate();
        let hour = date.getHours();
        let minute = date.getMinutes();
        let second = date.getSeconds();
        return `${year}/${month >= 10 ? month : "0"+month}/${day >= 10 ? day : "0"+day} ${hour >= 10 ? hour : "0"+hour}:${minute >= 10 ? minute : "0"+minute}:${second >= 10 ? second : "0"+second}`
    }
}