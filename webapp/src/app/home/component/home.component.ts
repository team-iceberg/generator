import {Component} from '@angular/core';
import {NgxFileDropEntry, FileSystemFileEntry} from 'ngx-file-drop';
import {RegistrationGeneratorService} from '../../services/registration-generator.service';

@Component({
    selector: 'home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.less']
})
export class HomeComponent {

    public files: NgxFileDropEntry[] = [];

    constructor(private registrationGeneratorService: RegistrationGeneratorService) {
    }

    public dropped(files: NgxFileDropEntry[]) {
        files.every(droppedFile => {
            if (droppedFile.fileEntry.isFile) {
                const fileEntry = droppedFile.fileEntry as FileSystemFileEntry;
                fileEntry.file((file: File) => {
                    if (this.isXlsxFile(file.name)) {
                        this.registrationGeneratorService.generateRegistration(file);
                    } else {
                        console.log('Le fichier doit être un fichier excel .XLSX')
                    }
                });
            } else {
                console.log('Ce n\'est pas un fichier');
                return;
            }
        });
    }

    private isXlsxFile(name: string) {
        const splitName = name.split('.');
        const extension = splitName[splitName.length - 1];
        return extension === 'xlsx';
    }
}
