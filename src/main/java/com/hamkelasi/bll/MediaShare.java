package com.hamkelasi.bll;

import com.hamkelasi.dal.Base;
import org.springframework.jdbc.core.JdbcTemplate;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class MediaShare {
    private int id;
    private int userId;
    private int schoolYearId;
    private String subject;
    private String description;
    private String filename;
    private String serverMap;
    private String url;
    private String thumbnailUrl;
    private LocalDateTime postDate;
    private Types shareType;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSchoolYearId() {
        return schoolYearId;
    }

    public void setSchoolYearId(int schoolYearId) {
        this.schoolYearId = schoolYearId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getServerMap() {
        return serverMap;
    }

    public void setServerMap(String serverMap) {
        this.serverMap = serverMap;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public Types getShareType() {
        return shareType;
    }

    public void setShareType(Types shareType) {
        this.shareType = shareType;
    }

    public MediaShare() {
    }

    public MediaShare(int id) {
        com.hamkelasi.dal.MediaShare dalMediaShare = new com.hamkelasi.dal.MediaShare();
        List<Base.Row> dt = dalMediaShare.getRecord(id);

        if (!dt.isEmpty()) {
            this.id = Integer.parseInt(dt.get(0).get("ID").toString());
            this.userId = Integer.parseInt(dt.get(0).get("UserID").toString());
            this.schoolYearId = Integer.parseInt(dt.get(0).get("SchoolYearID").toString() );
            this.subject = dt.get(0).get("Subject").toString();
            this.description = dt.get(0).get("Description").toString();
            this.url = dt.get(0).get("URL").toString();
            this.thumbnailUrl = dt.get(0).get("thumbURL").toString();
            this.postDate = LocalDateTime.parse(dt.get(0).get("PostDate").toString());
            this.shareType = Types.fromValue(Integer.parseInt(dt.get(0).get("ShareType").toString()));
        } else {
            this.id = -1;
        }
    }

    private List<MediaShare> getList(Types type, int schoolYearId) {
        com.hamkelasi.dal.MediaShare dalMediaShare = new com.hamkelasi.dal.MediaShare();
        List<Base.Row> dt = dalMediaShare.getListID(type.getValue(), schoolYearId);
        List<MediaShare> mediaShareList = new ArrayList<>();

        for (int i = 0; i < dt.size(); i++) {
            int tempId = Integer.parseInt(dt.get(i).get("id").toString());
            MediaShare mediaTemp = new MediaShare(tempId);
            mediaShareList.add(mediaTemp);
        }

        return mediaShareList;
    }

    public List<MediaShare> getPictures(int schoolYearId) {
        return getList(Types.PICTURE, schoolYearId);
    }

    public List<MediaShare> getVideos(int schoolYearId) {
        return getList(Types.VIDEO, schoolYearId);
    }

    public List<MediaShare> getSounds(int schoolYearId) {
        return getList(Types.SOUND, schoolYearId);
    }

    private int getCount(Types type, int schoolYearId) {
        com.hamkelasi.dal.MediaShare dalMediaShare = new com.hamkelasi.dal.MediaShare();
        return dalMediaShare.getCount(type.getValue(), schoolYearId);
    }

    public int getPictureCount(int schoolYearId) {
        return getCount(Types.PICTURE, schoolYearId);
    }

    public int getVideoCount(int schoolYearId) {
        return getCount(Types.VIDEO, schoolYearId);
    }

    public int getSoundCount(int schoolYearId) {
        return getCount(Types.SOUND, schoolYearId);
    }

    public int add() {
        if (this.shareType == Types.PICTURE) {
            // Create thumbnail for picture
            BufferedImage thumbPicture = createThumbnail(this.serverMap + this.filename, 100, 100);

            String extension = this.filename.substring(this.filename.lastIndexOf("."));
            String filenameWithoutExt = this.filename.substring(0, this.filename.lastIndexOf("."));

            if (thumbPicture != null) {
                try {
                    File thumbFile = new File(this.serverMap + filenameWithoutExt + "_thumb" + extension);
                    ImageIO.write(thumbPicture, extension.substring(1).toLowerCase(), thumbFile);
                    this.url = "Sharing/" + this.filename;
                    this.thumbnailUrl = "Sharing/" + filenameWithoutExt + "_thumb" + extension;
                } catch (IOException e) {
                    this.url = "Sharing/" + this.filename;
                    this.thumbnailUrl = "Sharing/default_thumb.png"; // Fallback
                }
            } else {
                this.url = "Sharing/" + this.filename;
                this.thumbnailUrl = "Sharing/default_thumb.png"; // Fallback
            }
        } else if (this.shareType == Types.VIDEO) {
            // Create thumbnail for video using external tool (e.g., FFmpeg)
            String filename = this.filename;
            String imageName = this.filename.substring(0, this.filename.lastIndexOf(".")) + "_thumb.jpg";
            String path = this.serverMap;

            try {
                ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-ss", "1", "-i", filename, "-f", "image2", "-s", "100x75", "-vframes", "1", imageName);
                pb.directory(new File(path));
                pb.redirectErrorStream(true);
                Process process = pb.start();
                process.waitFor();
                this.url = "Sharing/" + filename;
                this.thumbnailUrl = "Sharing/" + imageName;
            } catch (IOException | InterruptedException e) {
                this.url = "Sharing/" + this.filename;
                this.thumbnailUrl = "Sharing/default_video_thumb.jpg"; // Fallback
            }
        } else if (this.shareType == Types.SOUND) {
            // Create thumbnail for sound
            this.url = "Sharing/" + this.filename;
            this.thumbnailUrl = "Sharing/sound-default.png";
        }

        com.hamkelasi.dal.MediaShare newMedia = new com.hamkelasi.dal.MediaShare();
        boolean isInserted = newMedia.add(this.userId, this.schoolYearId, this.subject, this.description,
                this.url, this.thumbnailUrl, this.postDate, this.shareType.getValue());

        if (isInserted) {
            return 0; // ثبت اطلاعات با موفقیت انجام گرفت
        } else {
            return 9; // اشکال در ثبت اطلاعات
        }
    }

    private BufferedImage createThumbnail(String fileName, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(fileName));
            if (originalImage.getWidth() < width && originalImage.getHeight() < height) {
                return originalImage;
            }

            int newWidth = 0;
            int newHeight = 0;
            double ratio;

            if (originalImage.getWidth() > originalImage.getHeight()) {
                ratio = (double) width / originalImage.getWidth();
                newWidth = width;
                newHeight = (int) (originalImage.getHeight() * ratio);
            } else {
                ratio = (double) height / originalImage.getHeight();
                newHeight = height;
                newWidth = (int) (originalImage.getWidth() * ratio);
            }

            BufferedImage thumbImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = thumbImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setColor(java.awt.Color.WHITE);
            g.fillRect(0, 0, newWidth, newHeight);
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();
            originalImage.flush();

            return thumbImage;
        } catch (IOException e) {
            return null;
        }
    }

    // Enum equivalent to C# MediaShare.Types
    public enum Types {
        PICTURE(1),
        VIDEO(2),
        SOUND(3);

        private final int value;

        Types(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Types fromValue(int value) {
            for (Types type : Types.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid Types value: " + value);
        }
    }

}