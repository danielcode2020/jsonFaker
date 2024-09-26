export interface IUser {
  id : number;
  name : string;
  username : string;
  email : string;
  phone : string;
  website : string;
  address : Address;
  company : Company;
}


interface Address{
  id : number;
  street : string;
  city : string;
  suite : string;
  zip : string;
  geo : Geo;
}

interface Company{
  id : number;
  name : string;
  catchPhrase : string;
  bs : string;
}


interface Geo{
  id : number;
  lat : number;
  lng : number;
}
