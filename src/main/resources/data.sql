INSERT INTO public."role"
(id, activo, nombre)
VALUES(0, true, 'Socio');

INSERT INTO public."role"
(id, activo, nombre)
VALUES(1, true, 'Presidente');

INSERT INTO public."role"
(id, activo, nombre)
VALUES(2, true, 'Secretario');

INSERT INTO public."role"
(id, activo, nombre)
VALUES(3, true, 'Tesorero');

INSERT INTO public."role"
(id, activo, nombre)
VALUES(4, true, 'Vocal');

INSERT INTO public."role"
(id, activo, nombre)
VALUES(5, true, 'SuperAdmin');

INSERT INTO public.beneficio
(id, activo, nombre)
VALUES(1, true, 'Accion');

INSERT INTO public.beneficio
(id, activo, nombre)
VALUES(2, true, 'Horas');

INSERT INTO public.beneficio
(id, activo, nombre)
VALUES(3, true, 'Por Persona');

INSERT INTO public.ciudadanos
(id, activo, direccion, fecha_nacimiento, fecha_creacion, curp, descripcion, nativo, nombre, fecha_actualizacion, usuario_editor, casado)
VALUES(0, true, null, now(), now(), 'AAAA000000HHHHHH00', null, true, 'Administrador de sistema', now(), 0, true);

INSERT INTO public.users
(id, active, user_name, "password", ciudadano_id)
VALUES(0, true, 'admin', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 0);

INSERT INTO public.comitivas
(id, activo, fecha_actualizacion, fecha_creacion, nombre, usuario_editor, unidad_beneficio_id)
VALUES(0, true, null, now(), 'Administrador', null, 1);

INSERT INTO public.administradores
(id, activo, fecha_actualizacion, fecha_creacion, role_id, usuario_editor, ciudadano_id, comitiva_id)
VALUES(0, true, null, now(), 5, null, 0, 0);
