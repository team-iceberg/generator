import {NgModule} from '@angular/core';
import {MatProgressSpinnerModule, MatIconModule, MatSnackBarModule, MatButtonModule} from '@angular/material';

@NgModule({
    imports: [
        MatProgressSpinnerModule,
        MatIconModule,
        MatSnackBarModule,
        MatButtonModule
    ],
    exports: [
        MatProgressSpinnerModule,
        MatIconModule,
        MatSnackBarModule,
        MatButtonModule
    ]
})
export class MaterialModule {
}
