import { Component } from "@angular/core";
import { RouterOutlet } from "@angular/router";
import { AppHeader } from "./header.component";
import { AppSidebar } from "./sidebar.component";

@Component({
    selector: "app-layout",
    templateUrl: "./layout.component.html",
    imports: [RouterOutlet, AppHeader, AppSidebar]
})
export class AppLayout {
    
}