import { HttpClient } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";

@Injectable({ providedIn: 'root'})
export class HttpService{
    headers: object;
    baseUrl: string;
    constructor(@Inject(HttpClient) private httpClient: HttpClient) {
        this.headers = {
            timeout: 30000
        }
        this.baseUrl = "http://localhost:8080/"
        //this.baseUrl = "";
    }

    public get(url:string, headers:{[key:string]:any}, params:{[key:string]:any}, callback?:Function, errorCallback?:Function, finallyCallback?:Function):void{
        let obs = this.httpClient.get(`${this.baseUrl}${url}`, {
            headers: {
                ...this.headers,
                ...headers,
            },
            params
        });
        let me = this;
        obs.subscribe({
            next: (response)=>{
                if(callback){
                    callback(response);
                }
            },
            error: (error)=>{
                if(errorCallback){
                    errorCallback(error);
                }
                if(finallyCallback){
                    finallyCallback();
                }
            },
            complete: ()=>{
                if(finallyCallback){
                    finallyCallback();
                }
            }
        })
    }

    public post(url:string, headers:{[key:string]:any}, data: any,params?:{[key:string]:any}, callback?:Function, errorCallback?:Function, finallyCallback?:Function):void{
        let obs = this.httpClient.post(`${this.baseUrl}${url}`,data, {
            headers: {
                ...this.headers,
                ...headers
            },
            params
        });
        obs.subscribe({
            next: (response)=>{
                if(callback){
                    callback(response);
                }
            },
            error: (error)=>{
                if(errorCallback){
                    errorCallback(error);
                }
                if(finallyCallback){
                    finallyCallback();
                }
            },
            complete: ()=>{
                if(finallyCallback){
                    finallyCallback();
                }
            }
        })
    }
}