import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {ReactiveFormsModule, FormsModule} from '@angular/forms';
import {NgxFileDropModule} from 'ngx-file-drop';
import {RegistrationGeneratorService} from '../services/registration-generator.service';
import {MaterialModule} from '../shared/material/material.module';
import {SharedModule} from '../shared/shared.module';
import {HomeComponent} from './component/home.component';
import {HomeRoutingModule} from './home-routing.module';

@NgModule({
    declarations: [
        HomeComponent
    ],
    imports: [
        HomeRoutingModule,
        CommonModule,
        ReactiveFormsModule,
        NgxFileDropModule,
        FormsModule,
        MaterialModule
    ],
    exports: [],
    providers: [
        RegistrationGeneratorService
    ],
    bootstrap: [HomeComponent]
})

export class HomeModule {
}
