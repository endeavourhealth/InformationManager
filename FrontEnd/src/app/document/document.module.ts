import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {DocumentService} from './document.service';
import {DocumentComponent} from './document.component';
import {ControlsModule} from 'eds-angular4/dist/controls';
import {PublishDialogComponent} from './publish-dialog/publish-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgbModule,
    ControlsModule
  ],
  declarations: [
    DocumentComponent,
    PublishDialogComponent
  ],
  providers: [DocumentService],
  entryComponents: [PublishDialogComponent]
})
export class DocumentModule { }
