import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SpinnerStore} from '../tools/stores/spinner/spinner.store';
import {MaterialModule} from './material/material.module';
import {SpinnerComponent} from './spinner/spinner.component';


@NgModule({
    declarations: [
        SpinnerComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        MaterialModule
    ],
    entryComponents: [SpinnerComponent],
    exports: [SpinnerComponent],
    providers: [SpinnerStore]
})

export class SharedModule {
}




