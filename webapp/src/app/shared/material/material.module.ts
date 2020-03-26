import {NgModule} from '@angular/core';
import {MatProgressSpinnerModule, MatIconModule} from '@angular/material';

@NgModule({
    imports: [
        MatProgressSpinnerModule,
        MatIconModule
    ],
    exports: [
        MatProgressSpinnerModule,
        MatIconModule
    ]
})
export class MaterialModule {
}
