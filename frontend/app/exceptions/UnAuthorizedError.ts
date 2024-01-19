export class UnAuthorizedError extends Error {
  constructor(msg: string) {
    super(msg);
    Object.setPrototypeOf(this, UnAuthorizedError.prototype);
  }
}
