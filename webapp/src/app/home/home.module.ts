import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {RegistrationGeneratorService} from '../services/registration-generator.service';
import {HomeComponent} from './component/home.component';
import {HomeRoutingModule} from './home-routing.module';

@NgModule({
    declarations: [
        HomeComponent
    ],
    imports: [
        HomeRoutingModule,
        CommonModule,
        ReactiveFormsModule
    ],
    exports: [],
    providers: [
        RegistrationGeneratorService
    ],
    bootstrap: [HomeComponent]
})

export class HomeModule {
}
