import { Component, OnInit } from "@angular/core";
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { CommonModule } from "@angular/common";
import { HttpService } from "../../services/http.service";
import { BlockUIModule } from 'primeng/blockui';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { HttpClientModule } from "@angular/common/http";

@Component({
    standalone: true,
    selector: "device-management",
    templateUrl: "./device.component.html",
    providers: [MessageService,HttpService],
    imports: [CommonModule,TableModule,ButtonModule,DialogModule,InputTextModule,FormsModule, ReactiveFormsModule,ToastModule,
        BlockUIModule, ProgressSpinnerModule, HttpClientModule
    ]
})
export class DeviceManagement implements OnInit{
    dataSet: {
        content: Array<any>,
        total: number
    } = {content: [], total: 0}
    rowFirst: number = 0;
    pageNumber: number = 0;
    pageSize: number = 5;
    totalRecord: number = 0;
    isOpenAddDevice: boolean = false;
    blockedDocument: boolean = false;
    device: {
        deviceName: string|null
    } = {deviceName: null}
    constructor(private messageService: MessageService, private httpService: HttpService){
        
    }

    ngOnInit(): void {
        this.onSearchDevice();
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

    onPage(event: any){
        this.rowFirst = event.first;
        this.pageSize = event.rows;
        this.pageNumber = this.rowFirst / this.pageSize;
    }

    reload(){
        this.onSearchDevice();
    }

    onSearchDevice(){
        let me = this;
        this.blockedDocument = true;
         this.httpService.get("get-all-device", {}, {page: this.pageNumber, size: this.pageSize}, (response: any)=>{
        //this.httpService.get("/data/devices.json", {}, {page: this.pageNumber, size: this.pageSize}, (response: any)=>{
            me.dataSet.content = response.content;
            me.dataSet.total = response.total;
            me.blockedDocument = false;
        }, ()=>{
            me.messageService.add({ severity: 'error', summary: 'Lỗi', detail: 'Tải danh sách thiết bị thất bại' });
        }, ()=>{
            me.blockedDocument = false;
        })
    }

    openFormAddDevice(){
        this.isOpenAddDevice = true;
        this.device = {deviceName: null}
    }

    addDevice(){
        let me = this;
        if(!this.device.deviceName){
            this.messageService.add({ severity: 'error', summary: 'Lỗi dữ liệu', detail: 'Tên thiết bị không được để trống!' });
            return;
        }
        if(!/^[\d\w\-_]+$/.test(this.device.deviceName)){
            this.messageService.add({ severity: 'error', summary: 'Lỗi dữ liệu', detail: 'Tên thiết bị chỉ chứa chữ cái la tinh, số, -, _' });
            return;
        }
        this.blockedDocument = true;
        this.httpService.post("create-device", {}, this.device, {}, (response: any)=>{
            me.isOpenAddDevice = false;
            me.messageService.add({ severity: 'success', summary: 'Thành công', detail: 'Tạo thiết bị thành công' });
            me.onSearchDevice();
        }, (error: any)=>{
            this.messageService.add({ severity: 'error', summary: 'Thất bại', detail: 'Tạo thiết bị thất bại' });
            this.blockedDocument = false;
        })
    }
}