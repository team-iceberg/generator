import {Component} from '@angular/core';
import {NgxFileDropEntry, FileSystemFileEntry} from 'ngx-file-drop';
import {RegistrationGeneratorService} from '../../services/registration-generator.service';
import {ToastService} from '../../shared/toast/toast.service';

@Component({
    selector: 'home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.less']
})
export class HomeComponent {

    public files: NgxFileDropEntry[] = [];

    constructor(private registrationGeneratorService: RegistrationGeneratorService, private toastService: ToastService) {
    }

    public dropped(files: NgxFileDropEntry[]) {
        files.every(droppedFile => {
            if (droppedFile.fileEntry.isFile && files.length === 1) {
                const fileEntry = droppedFile.fileEntry as FileSystemFileEntry;
                fileEntry.file((file: File) => {
                    if (this.isXlsxFile(file.name)) {
                        this.registrationGeneratorService.generateRegistration(file);
                    } else {
                        this.toastService.alert('Le fichier doit être un fichier excel .XLSX');
                    }
                });
            } else {
                this.toastService.alert('Ce n\'est pas un fichier');
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
