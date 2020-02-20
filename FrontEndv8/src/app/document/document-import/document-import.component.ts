import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpEventType } from '@angular/common/http';

@Component({
  selector: 'document-import',
  templateUrl: './document-import.component.html',
  styleUrls: ['./document-import.component.scss']
})

export class DocumentImportComponent implements OnInit {

  fileData: File = null;
  previewUrl:any = null;
  fileUploadProgress: string = null;
  isLoading: boolean = false;
  statusMsg: boolean = false;
  uploadedFilePath: string = null;
  constructor(private http: HttpClient) { }

  ngOnInit() {
  }

  fileProgress(fileInput: any) {
    this.fileData = <File>fileInput.target.files[0];
    this.preview();
  }

  preview() {
    // Show preview


    var reader = new FileReader();
    reader.readAsDataURL(this.fileData);
    reader.onload = (_event) => {
      this.previewUrl = reader.result;
    }
  }

  onSubmit() {
    this.isLoading = true;
    this.statusMsg = false;
    const formData = new FormData();
    formData.append('file', this.fileData);
    this.http.post('api/document/documentImport', formData)
      .subscribe(res => {
        console.log(res);
        this.isLoading = false;
        this.statusMsg = true;
        //this.uploadedFilePath = res.data.filePath;
      })
  }

  promptCreate() {

  }
}
