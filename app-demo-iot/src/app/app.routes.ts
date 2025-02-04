import { Routes } from '@angular/router';
import { AppLayout } from '../templates/layout/layout.component';
import { DeviceManagement } from '../templates/devices/device.component';
import { TelemetryDevice } from '../templates/telemetry/telemetry.component';
import { PageNotfound } from '../templates/pages/page.notfound';

export const routes: Routes = [
    {
        path: "", component: AppLayout, 
        children: [
            {path: "device", component: DeviceManagement},
            {path: "telemetry", component: TelemetryDevice}
        ]
    },
    { path: "404", component: PageNotfound},
    { path: "**", redirectTo: "/404"}
];
