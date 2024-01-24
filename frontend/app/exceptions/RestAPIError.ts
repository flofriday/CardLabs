export class RestAPIError extends Error {
  constructor(message: string) {
    super(message);
    this.name = "RestAPIError";
  }
}

