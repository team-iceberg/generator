import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SpinnerStore} from '../tools/stores/spinner/spinner.store';
import {MaterialModule} from './material/material.module';
import {SpinnerComponent} from './spinner/spinner.component';
import {CustomToastComponent} from './toast/custom-toast.component';
import {ToastService} from './toast/toast.service';


@NgModule({
    declarations: [
        SpinnerComponent,
        CustomToastComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        MaterialModule
    ],
    entryComponents: [SpinnerComponent, CustomToastComponent],
    exports: [SpinnerComponent],
    providers: [SpinnerStore, ToastService]
})

export class SharedModule {
}




