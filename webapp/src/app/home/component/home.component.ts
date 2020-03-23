import {Component} from '@angular/core';
import {RegistrationGeneratorService} from '../../services/registration-generator.service';

@Component({
    selector: 'home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.less']
})
export class HomeComponent {

    constructor(private registrationGeneratorService: RegistrationGeneratorService) {
    }

    uploadFile(files: FileList) {
        this.registrationGeneratorService.generateRegistration(files.item(0));
    }
}
