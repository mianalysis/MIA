clear

pnameIn = 'C:\Users\steph\Desktop\';
fnameIn = 'GiantBlobs.tif';

pnameOut = 'C:\Users\steph\Desktop\';
fnameOut = 'GiantBlobs.csv';

nZ = 1;
nT = 1;

for j=1:nT
    for i = 1:nZ
        im(:,:,i,j) = imread([pnameIn,fnameIn],(j-1)*nZ+i);
    end
end

[x,y,z,t] = ind2sub(size(im),find(im~=0));
numel(x)
coords = uint8(zeros(numel(x),7));
disp('Created array');
coords(:,3:4) = [y,x];
% for i=1:numel(x)
%    newCoord = [im(x(i),y(i),z(i),t(i)),im(x(i),y(i),z(i)),y(i)-1,x(i)-1,0,z(i)-1,t(i)-1];
%    coords(i,:) = newCoord;
%     
% end

disp('Writing');

csvwrite([pnameOut,fnameOut],coords);