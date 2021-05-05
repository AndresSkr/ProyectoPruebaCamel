INSERT INTO EMPLEADOS (NOMBRES, APELLIDOS, DIRECCION, TELEFONO, EMAIL, CARGO, SALARIO) 
VALUES 
(:#${body.nombres}, :#${body.apellidos}, :#${body.direccion}, :#${body.telefono}, :#${body.email}, :#${body.cargo}, :#${body.salario});
